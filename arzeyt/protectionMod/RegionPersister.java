package arzeyt.protectionMod;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;


public class RegionPersister {

	
	ProtectionMod instance;
	RegionHandler regionHandler;
	
	public RegionPersister(ProtectionMod instance){
		this.instance = instance;
		this.regionHandler = instance.regionHandler;
	}
	
	public void Serialize(){
		//create a new file
		File saveFile = new File(ProtectionMod.configDir);//file location
		if(!saveFile.exists()){
			try{
				new File(ProtectionMod.configDir).mkdir();
				saveFile.createNewFile();
			} catch( IOException e){
				e.printStackTrace();
			}
		}
		try{
			FileOutputStream fostream = new FileOutputStream(ProtectionMod.configDir + File.separator + "protection.dat");
			ObjectOutputStream oostream = new ObjectOutputStream(fostream);
			
			oostream.writeInt(regionHandler.regions.size());
			
			
			if(regionHandler.regions.isEmpty()){
				instance.myLogger.info("No regions to save");
			}else{
				try{
					for (ProtectionRegion region: regionHandler.regions){
						
						oostream.writeObject(region);
					}
				}catch(Exception e){
					instance.myLogger.info("serialization unsuccessful");
				}
			oostream.close();
			instance.myLogger.info("Signs successfully serialized");
			}
		}catch (IOException e){
			e.printStackTrace();
		}
	}
	
	public void Deserialize(){
		File saveFile = new File(ProtectionMod.configDir + File.separator + "protection.dat");
		boolean exists = saveFile.exists();
		
		if(exists){
			FileInputStream fistream = null;
			ObjectInputStream oistream = null;
			try{
				fistream = new FileInputStream(ProtectionMod.configDir + File.separator + "protection.dat");
				oistream = new ObjectInputStream(fistream);
				
				Integer recordCount = oistream.readInt();
				instance.myLogger.info("Loading "+ recordCount +"regions");
				
				for(int i = 0; i < recordCount; i++){
					ProtectionRegion sign = (ProtectionRegion)oistream.readObject();
					regionHandler.regions.add(sign);
				}
			}catch(FileNotFoundException e){
				instance.myLogger.info("Could not locate data file... ");
				e.printStackTrace();
			}catch(IOException e){
				instance.myLogger.info("Protection File is Empty");
			}catch(ClassNotFoundException e){
				instance.myLogger.info("Could not find class to load");
			}finally{
				try{
					oistream.close();
				}catch(IOException e){
					instance.myLogger.info("Error while trying to close input stream");
				}
			}
		}
	}
}