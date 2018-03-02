/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pk.projectcpu;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author PraveenKumar
 */
public class Loader {
    
    File file;
    public Loader(String filePath) {
        file = new File(filePath);
        
    }
    
    
    public BufferedReader getFileReader(){
        
        try {
            FileInputStream fstream = new FileInputStream(file);
            BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
            return br;
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Loader.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    public ArrayList getInstructionSet(BufferedReader reader){
        String strLine;
        String[] instructions;
        ArrayList<Instruction> instructionSet = new ArrayList();
        try {
            while ((strLine = reader.readLine()) != null)   {
                // Print the content on the console
                instructions = strLine.split(" ");
                instructionSet.add(new Instruction(Integer.parseInt(instructions[0]), Integer.parseInt(instructions[1]), Integer.parseInt(instructions[2])));
            }   } catch (IOException ex) {
            Logger.getLogger(Loader.class.getName()).log(Level.SEVERE, null, ex);
        }
        return instructionSet;
    }
    
    
}
