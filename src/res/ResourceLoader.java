/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package res;

import java.awt.Image;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import javax.imageio.ImageIO;

/**
 * A források betöltését végző osztály.
 *
 * @author bence
 */
public class ResourceLoader {

    /**
     * Betölti az adott forrást szöveges feldolgozásra.
     *
     * @param resName a fájl címe
     * @return a beolvasó stream objektum
     */
    public static InputStream loadResource(String resName) {
        return ResourceLoader.class.getClassLoader().getResourceAsStream(resName);
    }

    /**
     * Betölti az adott forrást képként.
     *
     * @param resName a fájl címe
     * @return a kép
     * @throws IOException
     */
    public static Image loadImage(String resName) throws IOException {
        URL url = ResourceLoader.class.getClassLoader().getResource(resName);
        return ImageIO.read(url);
    }
}
