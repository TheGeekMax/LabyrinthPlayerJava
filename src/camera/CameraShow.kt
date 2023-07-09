package camera

import java.awt.Graphics

interface CameraShow {
    fun showTile(g:Graphics, x:Int, y:Int, posX:Int, posY:Int)
    fun click(x:Int, y:Int)
}