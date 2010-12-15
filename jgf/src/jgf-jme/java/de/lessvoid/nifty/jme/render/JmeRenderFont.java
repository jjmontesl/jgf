package de.lessvoid.nifty.jme.render;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.logging.Logger;

import com.jmex.angelfont.BitmapCharacter;
import com.jmex.angelfont.BitmapFont;
import com.jmex.angelfont.BitmapFont.Align;
import com.jmex.angelfont.BitmapFontLoader;
import com.jmex.angelfont.BitmapText;
import com.jmex.angelfont.Kerning;

import de.lessvoid.nifty.elements.tools.FontHelper;
import de.lessvoid.nifty.spi.render.RenderDevice;
import de.lessvoid.nifty.spi.render.RenderFont;
import de.lessvoid.nifty.tools.resourceloader.ResourceLoader;

/**
 * <code>JmeRenderFont</code> is a Nifty RenderFont implementation for JME2
 * @author void
 * @author larynx
 */
public class JmeRenderFont implements RenderFont 
{
  public BitmapFont fnt = null;
  public BitmapText txt = null;
  public int size;
  
  // Added by larynx 2010.04.08
  // Add common logging and serial
  private static final Logger logger = Logger.getLogger(JmeRenderFont.class.getName());
  private static final long serialVersionUID = 1L;  
  
  
  /**
   * Initialize the font.
   * @param name font filename
   */
  public JmeRenderFont(final String name, final RenderDevice device) 
  {
      URL fontFile = ResourceLoader.getResource(name); 
      URL textureFile = null;
                    
      try 
      {
        // fetch the texturefile url from the fnt file
        textureFile = getTextureFile(fontFile);
        
        fnt = BitmapFontLoader.load(fontFile, textureFile);              
        
        txt = new BitmapText(fnt, false);
        
        //size = fnt.getCharSet().getRenderedSize();
        //size = fnt.getCharSet().getLineHeight();
        size = getSize();
        
        txt.setSize(size);
        txt.setAlignment(Align.Left);
      } 
      catch (IOException e) 
      {
          e.printStackTrace();
          logger.severe(e.getMessage());
      } // catch
        
  }
    
  public int getHeight() 
  {   	  
	  //return (int)txt.getHeight();
	  return (int)fnt.getCharSet().getLineHeight();
	  //return fnt.getCharSet().getLineHeight() * (100 / fnt.getCharSet().getRenderedSize());
  }
  
  
  public int getSize() 
  {   
	  return (int)fnt.getCharSet().getRenderedSize();
  }


  public int getWidth(final String text) 
  {
    if (text.length() > 0)
    {
      return (int)getLineWidth(text);
    }
    else
      return 0;
  }
  
  public float getLineWidth(String text)
  {
      float lineWidth = 0f;
      char lastChar = 0;
      char theChar = 0;
      boolean firstCharOfLine = true;
//      float sizeScale = (float) block.getSize() / charSet.getRenderedSize();
      float sizeScale = 1f;
      int amount = 0;
      for (int i = 0; i < text.length(); i++)
      {
          theChar = text.charAt(i);
          BitmapCharacter c = fnt.getCharSet().getCharacter((int) theChar);
          if (c != null)
          {
              if (!firstCharOfLine)
              {            	  
            	  // kerning amount
            	  amount = findKerningAmount(lastChar, theChar);
                  lineWidth += amount * sizeScale;                                    
              }
              float xAdvance = c.getXAdvance() * sizeScale;
              lineWidth += xAdvance;
              
              lastChar = theChar;
          }
          firstCharOfLine = false;
      }
      return lineWidth;
  }      
  

  public Integer getCharacterAdvance(final char currentCharacter, final char nextCharacter, final float size) 
  {
	  BitmapCharacter c = fnt.getCharSet().getCharacter(currentCharacter);
	  if (c == null) 
	  {
		  return null;
	  } 
	  else 
	  {
		  return new Integer((int)(c.getXAdvance() * size + findKerningAmount(currentCharacter, nextCharacter)));
	  }
  }
  
  /**
   * <code>getStringWidth</code> returns the text width according to size
   * @param text text
   * @param size size
   * @return length
   */
  public int getStringWidth(final String text, final float size) 
  {
	  int length = 0;

	  for (int i=0; i < text.length(); i++) 
	  {
		  char currentCharacter = text.charAt(i);
		  char nextCharacter = FontHelper.getNextCharacter(text, i);

		  Integer w = getCharacterAdvance(currentCharacter, nextCharacter, size);
		  if (w != null) 
		  {
			  length += w;
		  }
	  }
	  return length;
  }
  
  /**
   * <code>findKerningAmount</code> tries to find the kerning amount in pixel for this two chars
   * @param lastChar
   * @param nextChar
   * @return kerning amount if found, 0 else
   */
  private int findKerningAmount(int lastChar, int nextChar) 
  {
	  int amount = 0;
      BitmapCharacter c = fnt.getCharSet().getCharacter(lastChar);
      if(c==null) {
          return 0;
      }
      for (Kerning k : c.getKerningList()) 
      {
          if (k.getSecond() == nextChar) 
          {
              amount = k.getAmount();
              break;
          } 
      }  
      return amount;
  }
  /**
   * <code>getTextureFile</code> reads the name of the texture file from the font file
   * @param fontFile
   * @return
   */
  private URL getTextureFile(URL fontFile) throws IOException
  {
	  URL textureFile = null;
    
      BufferedReader reader = new BufferedReader(new InputStreamReader(fontFile.openStream()));
      String regex = "[\\s=]+";
      while (reader.ready()) 
      {
          String line = reader.readLine();
          String[] tokens = line.split(regex);
          if (tokens[0].equals("page")) 
          {
            // Get the file token from the page record
              // page id=0 file="console_00.png" 
              for (int i = 1; i < tokens.length; i++) 
              {
                  if (tokens[i].equals("file")) 
                  {
                    String fileName = tokens[i + 1].replaceAll("\"",""); 
                    textureFile = new URL(fontFile, fileName); 
                  }
                  if (textureFile != null)
                  {
                    break;
                  }
              }
          }
          if (textureFile != null)
          {
            break;
          }
      }
      reader.close();
      return textureFile;
  }

  public void dispose() 
  {
  }  
}