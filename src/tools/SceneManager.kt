package tools;

import java.util.*
import javax.swing.*

class SceneManager {
    private val scenes: LinkedList<Pair<String,JPanel>> = LinkedList()

    fun addScene(name:String,scene:JPanel){
        scenes.add(Pair(name,scene))
    }

    fun setActiveScene(name:String,frame:JFrame){
        var pan:JPanel? = null
        for(scn in scenes){
            if(scn.first == name){
                pan = scn.second
            }
        }

        frame.contentPane = pan
        pan?.requestFocusInWindow()
        frame.pack()
    }
}
