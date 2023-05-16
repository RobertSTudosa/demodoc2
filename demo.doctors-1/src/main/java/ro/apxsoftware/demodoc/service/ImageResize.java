package ro.apxsoftware.demodoc.service;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.MetadataException;
import com.drew.metadata.Tag;
import com.drew.metadata.exif.ExifIFD0Directory;

import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.filters.Rotation;

@Service
public class ImageResize {
	
		
	    byte[] newBytes = null;
	    
	    public ByteArrayOutputStream resizeImage(MultipartFile img) {
	    	

	    	
	    	
	    	
	    	Metadata metadata = null;
	    	InputStream inputStream;
	    	int orientation = 0;
			try {
				inputStream = new BufferedInputStream(img.getInputStream());
				byte[] prepBytes = img.getBytes();
				
				//get the metadata 
				
				try {
					
					metadata = ImageMetadataReader.readMetadata(new BufferedInputStream(new ByteArrayInputStream(prepBytes)));
//					metadata = ImageMetadataReader.readMetadata(inputStream);
					System.out.println("passed metadata readMetadata");
					
					final ExifIFD0Directory exifIFD0 =  metadata.getFirstDirectoryOfType(ExifIFD0Directory.class);
					
					
					if(exifIFD0 != null  && exifIFD0.containsTag(ExifIFD0Directory.TAG_ORIENTATION) ) {
						orientation = exifIFD0.getInt(ExifIFD0Directory.TAG_ORIENTATION);
						System.out.println("orientation is : " + orientation);
						

					}
					
	

					
					
					for (Directory directory : metadata.getDirectories()) {
					    for (Tag tag : directory.getTags()) {
					        System.out.format("[%s] - %s = %s",
					            directory.getName(), tag.getTagName(), tag.getDescription());
					    }
					    if (directory.hasErrors()) {
					        for (String error : directory.getErrors()) {
					            System.err.format("ERROR: %s", error);
					        }
					    }
					}
					
				} catch (ImageProcessingException | IOException  | MetadataException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			
			
			
	    		
	    	ByteArrayOutputStream baos = new ByteArrayOutputStream();
	    	
	    	String imgName = StringUtils.cleanPath(img.getOriginalFilename());

	    	System.out.println("in resizeImage(MultipartFile method");
	    	
	    	  try {
	  			
	  			byte[] bytes = img.getBytes();
	  			
	  		
	  						
	  			BufferedImage imgBuf = ImageIO.read(new ByteArrayInputStream(bytes));
	  			  			
	  			int endIndexOfString = img.getContentType().length();
	  			
	  			String imageFormat = img.getContentType().substring(6,endIndexOfString);
	  		    
	  			if(orientation != 0) {
	  				
	  			
	  			switch (orientation) {
	  		  case 1: // [Exif IFD0] Orientation - Top, left side (Horizontal / normal)
	  			Thumbnails.of((BufferedImage)imgBuf)
  		    	.size(imgBuf.getWidth()/2, imgBuf.getHeight()/2)
  		    	.outputFormat(imageFormat)
  		    	.outputQuality(0.4)
//  		    	.rotate(0)
  		    	.toOutputStream(baos);
	  		  case 6: // [Exif IFD0] Orientation - Right side, top (Rotate 90 CW)
	  			Thumbnails.of((BufferedImage)imgBuf)
  		    	.size(imgBuf.getWidth()/2, imgBuf.getHeight()/2)
  		    	.outputFormat(imageFormat)
  		    	.outputQuality(0.4)
  		    	.rotate(90)
  		    	.toOutputStream(baos);
	  		  case 3: // [Exif IFD0] Orientation - Bottom, right side (Rotate 180)
	  			Thumbnails.of((BufferedImage)imgBuf)
  		    	.size(imgBuf.getWidth()/2, imgBuf.getHeight()/2)
  		    	.outputFormat(imageFormat)
  		    	.outputQuality(0.4)
  		    	.rotate(180)
  		    	.toOutputStream(baos);
	  		  case 8: // [Exif IFD0] Orientation - Left side, bottom (Rotate 270 CW)
	  			Thumbnails.of((BufferedImage)imgBuf)
  		    	.size(imgBuf.getWidth()/2, imgBuf.getHeight()/2)
  		    	.outputFormat(imageFormat)
  		    	.outputQuality(0.4)
  		    	.rotate(270)
  		    	.toOutputStream(baos);
	  		}
	  			} else {
	  				Thumbnails.of((BufferedImage)imgBuf)
	  		    	.size(imgBuf.getWidth()/2, imgBuf.getHeight()/2)
	  		    	.outputFormat(imageFormat)
	  		    	.outputQuality(0.8)
//	  		    	.rotate(0)
	  		    	.toOutputStream(baos);
	  			}
	  			
	  			
	     		      		  
	    	   } catch (IOException e) {
		  	    	e.printStackTrace();
		  	    }
	  		    
	  		    
	    	return baos;
	    
	    }
	    
	  
	    
	}

