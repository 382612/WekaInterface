package nl.bioinf.wekainterface.service;

import nl.bioinf.wekainterface.model.AlgortihmsInformation;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.Arrays;

/**
 * This class is a setup for the SerializationAlgorithmInformation
 * @author Bart Engels
 */

@Service
public class SerializationService {
    public void serialization(Object e) {
        try {
            FileOutputStream fileOut =
                    new FileOutputStream("/tmp/algorithmeData.ser");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(e);
            out.close();
            fileOut.close();
            System.out.print("Serialized data is saved in /tmp/algorithmeData.ser");
        } catch (IOException i) {
            i.printStackTrace();
        }
    }

    public AlgortihmsInformation deserialization(){
    AlgortihmsInformation e = null;
        try {
        FileInputStream fileIn = new FileInputStream("/tmp/algorithmeData.ser");
        ObjectInputStream in = new ObjectInputStream(fileIn);
        e = (AlgortihmsInformation) in.readObject();
        in.close();
        fileIn.close();
        return e;
    } catch (IOException | ClassNotFoundException i) {
        i.printStackTrace();
       throw new RuntimeException("oops");
    }}
}



