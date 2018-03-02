/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pk.projectcpu;

import java.util.ArrayList;

/**
 *
 * @author PraveenKumar
 */
public class Run {
    
    int pc;
    int accumulator;
    Instruction mbr;
    int opr;
    int i;
    int mar;
    int f = 0;
    int r = 0;
    ArrayList<Instruction> instructionSet;
    Loader ld;
    int[] memory = {2,3,1,2,0,0,0,0,90,100,110,120,130,150};
    public static void main(String args[]){
       String filePath ="src/contents.txt";
       
       Run r = new Run(); 
       r.initializeInstructions(filePath);
        
//        for(int i =0;i<instructionSet.size();i++){
//            System.out.println(instructionSet.get(i).getDirectBit());
//            System.out.println(instructionSet.get(i).getAddress());
//            System.out.println(instructionSet.get(i).getOpCode());
//        }
    }
    public void initializeInstructions(String filePath){
        ld = new Loader(filePath);
        instructionSet = ld.getInstructionSet(ld.getFileReader());
        performNextCycle();
    }
    public void performFetchCycle(){
        if(pc < instructionSet.size()){
            mar = pc;
            mbr = instructionSet.get(mar);
            pc++;
            opr = mbr.getOpCode();
            i = mbr.getDirectBit();
            if(i == 1){
                r = 1;
                f = 0;
                performIndirectCycle();
            }else{
                f = 1;
                r = 0;
                performExecuteCycle();
            }
        }
        
    }
    public void performIndirectCycle(){
        mar = mbr.getAddress();
        mbr.setAddress(memory[mar]);
        f = 1;
        r = 0;
        performNextCycle();
    }
    public void performExecuteCycle(){
        switch(opr){
            case 0:
                performAND();
                break;
            case 1:
                performADD();
                break;
            case 10:
                performLDA();
                break;
            case 11:
                performSTA();
                break;
            case 100:
                performBUN();
                break;
            case 101:
                performBSA();
                break;
            case 110:
                performISZ();
                break;
            case 111:
                performRef();
                break;
            default:
                System.out.println("LOL");
        }
    }
    
    public void performAND(){
        mar = mbr.getAddress();
        mbr.setAddress(memory[mar]);
        accumulator = accumulator & mbr.getAddress();
        f = 0;
        performNextCycle();
    }
    public void performADD(){
        mar = mbr.getAddress();
        mbr.setAddress(memory[mar]);
        accumulator = accumulator + mbr.getAddress();
        f = 0;
        performNextCycle();
    }
    public void performLDA(){
        mar = mbr.getAddress();
        mbr.setAddress(memory[mar]);
        accumulator = accumulator + mbr.getAddress();
        f = 0;
        performNextCycle();
    }
    public void performSTA(){
        mar = mbr.getAddress();
        mbr.setAddress(accumulator);
        memory[mar] = mbr.getAddress();
        f = 0;
        performNextCycle();
    }
    public void performBUN(){
        // TODO:
        
    }
    public void performBSA(){
        
    }
    public void performISZ(){
        
    }
    public void performRef(){
        
    }
    public void performNextCycle(){
        if(f==0 && r==0){
            performFetchCycle();
        }else if(f==0 && r==1){
            performIndirectCycle();
        }else if(f==1 && r==0){
            performExecuteCycle();
        }
    }
}
