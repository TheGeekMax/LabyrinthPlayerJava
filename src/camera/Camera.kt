package camera

import tools.Vector2Int
import java.awt.Color
import java.awt.Graphics

class Camera(private var cw: Int,
             private val tilemapWidth:Int, private val tilemapHeight:Int,
             private var screenWidth:Int, private var screenHeight:Int,
             private val radius:Float) {

    //variable internes de l'objet
    var camX:Float
    var camY:Float
    var playerX:Float
    var playerY:Float
    var isInBorder:Boolean

    //variable de calculs
    private var minX:Float
    private var minY:Float

    private var maxX:Float
    private var maxY:Float

    private var hitboxTileMap:Array<BooleanArray>
    private var showHitbox = false

    init{
        camX = (tilemapWidth*cw)/2f
        camY = (tilemapHeight*cw)/2f
        playerX = (tilemapWidth*cw)/2f
        playerY = (tilemapHeight*cw)/2f

        minX = 0f
        minY = 0f
        maxX = 0f
        maxY = 0f
        isInBorder = false
        calculateBorder()

        hitboxTileMap = Array(tilemapWidth) {BooleanArray(tilemapHeight) {false}}
    }

    private fun clamp(valeur:Float,min:Float,max:Float):Float{
        return Math.max(min,Math.min(valeur,max))
    }

    private fun isHit(x:Int,y:Int):Boolean{
        if(x < 0 || x >= tilemapWidth || y < 0 || y >= tilemapHeight){
            return true
        }
        return hitboxTileMap[x][y]
    }

    fun setShowHitbox(value:Boolean){
        showHitbox = value
    }

    fun setHitboxAt(x:Int,y:Int,value:Boolean){
        hitboxTileMap[x][y] = value
    }

    fun updateHitboxAt(x:Int,y:Int){
        hitboxTileMap[x][y] = !hitboxTileMap[x][y]
    }

    fun calculateBorder(){
        minX = screenWidth/2f
        minY = screenHeight/2f

        maxX = tilemapWidth*cw - screenWidth/2f
        maxY = tilemapHeight*cw - screenHeight/2f
    }

    fun UpdateScreenSize(w:Int,h:Int){
        //todo
    }

    fun updateCoors(x:Float,y:Float){
        var x = x*cw
        var y = y*cw

        var newplayerX = playerX
        var newplayerY = playerY


        //on regarde le cas x
        val pt:Pair<Int,Int> = globalToGridcoors(playerX,playerY)
        if(x > 0){
            val pt1 = globalToGridcoors(playerX + radius*cw + x,playerY - radius*cw)
            val pt2 = globalToGridcoors(playerX + radius*cw + x,playerY + radius*cw)
            if(isHit(pt1.first,pt1.second) || isHit(pt2.first,pt2.second)){
                //hit !
                newplayerX = (pt.first + 1) * cw - radius*cw-1
            }else{
                newplayerX += x
            }
        }else{ // x < 0
            val pt1 = globalToGridcoors(playerX - radius*cw + x,playerY - radius*cw)
            val pt2 = globalToGridcoors(playerX - radius*cw + x,playerY + radius*cw)
            if(isHit(pt1.first,pt1.second) || isHit(pt2.first,pt2.second)){
                //hit !
                newplayerX = (pt.first) * cw + radius*cw+1
            }else{
                newplayerX += x
            }
        }

        if(y > 0){
            val pt1 = globalToGridcoors(playerX - radius*cw,playerY + radius*cw + y)
            val pt2 = globalToGridcoors(playerX + radius*cw,playerY + radius*cw + y)
            if(isHit(pt1.first,pt1.second) || isHit(pt2.first,pt2.second)){
                //hit !
                newplayerY = (pt.second + 1) * cw - radius*cw-1
            }else{
                newplayerY += y
            }
        }else{ // y < 0
            val pt1 = globalToGridcoors(playerX - radius*cw,playerY - radius*cw + y)
            val pt2 = globalToGridcoors(playerX + radius*cw,playerY - radius*cw + y)
            if(isHit(pt1.first,pt1.second) || isHit(pt2.first,pt2.second)){
                //hit !
                newplayerY = (pt.second) * cw + radius*cw+1
            }else{
                newplayerY += y
            }
        }
        playerX = newplayerX
        playerY = newplayerY

        camX = playerX
        camY = playerY

        camX = clamp(camX,minX,maxX)
        camY = clamp(camY,minY,maxY)

        isInBorder = (camX == minX || camX == maxX || camY == minY || camY == maxY)
    }

    fun getBoundCoors():Pair<Vector2Int, Vector2Int> {
        var minCoors: Vector2Int =
            Vector2Int(((camX - (screenWidth / 2f)) / cw).toInt(), ((camY - (screenHeight / 2f)) / cw).toInt())
        var maxCoors: Vector2Int =
            Vector2Int(((camX + (screenWidth / 2f)) / cw).toInt() + 1, ((camY + (screenHeight / 2f)) / cw).toInt() + 1)
        maxCoors.x = Math.min(maxCoors.x,tilemapWidth-1)
        maxCoors.y = Math.min(maxCoors.y,tilemapWidth-1)

        //println(minCoors.x.toString()+" "+minCoors.y.toString())
        return Pair(minCoors,maxCoors)
    }

    fun getPLayerCanvasCoordinate():Pair<Int,Int> = Pair(truncate(screenWidth/2-camX+playerX),truncate(screenHeight/2-camY+playerY))

    fun showView(g:Graphics,cameT: CameraShow){
        val bounds :Pair<Vector2Int, Vector2Int> = getBoundCoors()
        var min: Vector2Int = bounds.first
        var max: Vector2Int = bounds.second
        for(i in min.x..max.x){
            for(j in min.y..max.y){
                cameT.showTile(g,i,j,i*cw-camX.toInt()+(screenWidth/2),j*cw-camY.toInt()+(screenHeight/2))
                if(showHitbox && hitboxTileMap[i][j]){
                    //on affiche un
                    g.setColor(Color.RED)
                    g.drawLine(i*cw-camX.toInt()+(screenWidth/2),j*cw-camY.toInt()+(screenHeight/2),i*cw-camX.toInt()+(screenWidth/2)+cw,j*cw-camY.toInt()+(screenHeight/2)+cw)
                    g.drawLine(i*cw-camX.toInt()+(screenWidth/2)+cw,j*cw-camY.toInt()+(screenHeight/2),i*cw-camX.toInt()+(screenWidth/2),j*cw-camY.toInt()+(screenHeight/2)+cw)

                }
            }
        }
    }

    private fun truncate(x:Float):Int{
        var value = 0;
        while(value < x) value++
        return value - 1
    }

    fun click(screenX:Float, screenY:Float,cameT: CameraShow):Pair<Int,Int>{
        val xTab:Float = ((screenX + camX - (screenWidth/2f))/cw)
        val yTab:Float = ((screenY + camY - (screenHeight/2f))/cw)
        cameT.click(truncate(xTab),truncate(yTab))
        return Pair<Int,Int>(truncate(xTab),truncate(yTab))
    }

    fun canvasToGlobalcoors(screenX:Float, screenY:Float):Pair<Int,Int>{
        val xTab:Float = ((screenX + camX - (screenWidth/2f))/cw)
        val yTab:Float = ((screenY + camY - (screenHeight/2f))/cw)
        return Pair<Int,Int>(truncate(xTab),truncate(yTab))
    }

    fun globalToGridcoors(Xcors:Float,Ycors:Float):Pair<Int,Int>{
        val xGrid:Int = truncate(Xcors/cw)
        val yGrid:Int = truncate(Ycors/cw)
        return Pair<Int,Int>(xGrid,yGrid)
    }

    fun zoom(value:Int){
        camX = (camX.toFloat()/cw)*(cw + value)
        camY = (camY.toFloat()/cw)*(cw + value)
        cw += value;
        calculateBorder()
    }
}