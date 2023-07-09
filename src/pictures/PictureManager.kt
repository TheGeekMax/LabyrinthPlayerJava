package pictures

import java.awt.image.BufferedImage
import java.io.File
import java.io.InputStream
import java.util.LinkedList
import javax.imageio.ImageIO

class PictureManager (url:InputStream,private val tile_width:Int){
    val mainPicture:BufferedImage
    private val pictures = LinkedList<Picture>()


    init {
        println(url)
        mainPicture = ImageIO.read(url)
    }

    fun addFromPicture(name:String,x:Int,y:Int){
        val newpic = Picture(name,mainPicture.getSubimage(x*tile_width,y*tile_width,tile_width,tile_width))
        pictures.add(newpic)
    }

    fun getBufferedPictureFromName(name:String):BufferedImage?{
        for(pic in pictures){
            if(pic.value == name){
                return pic.image
            }
        }
        return null
    }
}