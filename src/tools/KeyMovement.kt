package tools

import java.awt.event.KeyEvent

class KeyMovement(val keyUp:Int, val keyLeft:Int, val keyDown:Int, val keyRight:Int) {
    private var upPressed =false;
    private var leftPressed =false;
    private var downPressed =false;
    private var rightPressed =false;

    private var dx = 0;
    private var dy = 0;

    private fun calculateDc(){
        dx = 0
        dy = 0

        dy -= if (upPressed) 1 else 0
        dy += if (downPressed) 1 else 0
        dx -= if (leftPressed) 1 else 0
        dx += if (rightPressed) 1 else 0
    }

    fun getDirrection(): Vector2Int {
        return Vector2Int(dx, dy)
    }

    fun keyPressed(key:KeyEvent){
        when(key.keyCode){
            keyUp -> upPressed = true
            keyLeft -> leftPressed = true
            keyDown -> downPressed = true
            keyRight -> rightPressed = true
        }
        calculateDc()
    }

    fun keyReleased(key:KeyEvent){
        when(key.keyCode){
            keyUp -> upPressed = false
            keyLeft -> leftPressed = false
            keyDown -> downPressed = false
            keyRight -> rightPressed = false
        }
        calculateDc()
    }
}