package labyrinthe

import java.util.*
import kotlin.collections.ArrayDeque

class Labyrinthe(val usablewidth:Int, val usableheight: Int){
    val plateau:Array<BooleanArray>
    private val visited:Array<BooleanArray>

    init{
        plateau = Array(usablewidth*2 + 1) {BooleanArray(usableheight*2 + 1) {true}}
        visited = Array(usablewidth) {BooleanArray(usableheight) {false}}

        for(i in 1..usablewidth*2 step 2){
            for(j in 1 .. usableheight*2 step 2){
                plateau[i][j] = false
            }
        }
    }

    fun generate(startX:Int,startY:Int){
        val mainStack:ArrayDeque<Pair<Int,Int>> = ArrayDeque()
        mainStack.add(Pair(startX,startY))
        visited[startX][startY] = true

        while(mainStack.size > 0){
            val current = mainStack.removeLast()
            val neighbours = getNeighbour(current)
            if(neighbours.size >0){
                //il y a des voisins, on en visite un au pif
                mainStack.add(current)

                val elt = neighbours.random()
                removeWall(current,elt)
                visited[elt.first][elt.second] = true
                mainStack.add(elt)
            }
        }
    }

    private fun removeWall(v1:Pair<Int,Int>,v2:Pair<Int,Int>){
        val n1 = visitedToPlateauCoors(v1)
        val n2 = visitedToPlateauCoors(v2)

        plateau[(n1.first+n2.first)/2][(n1.second+n2.second)/2] = false
    }

    private fun getNeighbour(x:Pair<Int,Int>):LinkedList<Pair<Int,Int>>{
        val list:LinkedList<Pair<Int,Int>> = LinkedList()
        if(isValidNeighbour(x.first+1,x.second)) list.add(Pair(x.first+1,x.second))
        if(isValidNeighbour(x.first-1,x.second)) list.add(Pair(x.first-1,x.second))
        if(isValidNeighbour(x.first,x.second+1)) list.add(Pair(x.first,x.second+1))
        if(isValidNeighbour(x.first,x.second-1)) list.add(Pair(x.first,x.second-1))

        return list
    }
    private fun isValidNeighbour(x:Int,y:Int):Boolean{
        if(x < 0 || x >= usablewidth || y < 0 || y >= usableheight) return false
        return !visited[x][y]
    }

    private fun visitedToPlateauCoors(x:Pair<Int,Int>):Pair<Int,Int>{
        return Pair(x.first*2+1,x.second*2 +1)
    }
}