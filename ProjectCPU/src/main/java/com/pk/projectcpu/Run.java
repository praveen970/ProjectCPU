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
    
    int pc,completedProcesses = 0;
    int accumulator,extendedBit;
    Instruction mbr;
    int opr;
    int i;
    boolean switchProcess = false, isSingleProcess = true;
    int mar,count = 3,turn=1;
    int f = 0;
    int r = 0;
    ArrayList<Instruction> instructionSet1,instructionSet2;
    Loader ld1,ld2;
    ProcessControlBlock pcb1, pcb2;
    int[] memory = {2,3,1,20,30,0,0,0,90,100,110,120,130,150};
    public static void main(String args[]){
       //String filePath ="src/contents.txt";
        String filePath="", filePath2="";
        Run r = new Run(); 
        if(args.length>1){
            filePath = args[0];
            filePath2 = args[1]; 
            r.initializeInstructions(filePath,filePath2);
        }else{
            filePath = args[0];
            r.initializeInstructions(filePath);
        }
        
       
       
        
//        for(int i =0;i<instructionSet.size();i++){
//            System.out.println(instructionSet.get(i).getDirectBit());
//            System.out.println(instructionSet.get(i).getAddress());
//            System.out.println(instructionSet.get(i).getOpCode());
//        }
    }
    public void initializeInstructions(String filePath){
        ld1 = new Loader(filePath);
        isSingleProcess = true;
        instructionSet1 = ld1.getInstructionSet(ld1.getFileReader());
        
        performNextCycle();
    }
    public void initializeInstructions(String filePath, String filePath2){
        ld1 = new Loader(filePath);
        ld2 = new Loader(filePath2);
        isSingleProcess = false;
        pcb1 = new ProcessControlBlock(pc, mar, accumulator, extendedBit);
        pcb2 = new ProcessControlBlock(pc, mar, accumulator, extendedBit);
        instructionSet1 = ld1.getInstructionSet(ld1.getFileReader());
        instructionSet2 = ld2.getInstructionSet(ld2.getFileReader());
        performSwitching();
    }
    public void currentTurn(){
        if(count<=0){
            if(turn == 1){
                turn = 2;
                pcb1.setPc(pc);
                pcb1.setAccumulator(accumulator);
                pcb1.setExtendedBit(extendedBit);
                pcb1.setMar(mar);
                pc = pcb2.getPc();
                mar = pcb2.getMar();
                extendedBit = pcb2.getExtendedBit();
                accumulator = pcb2.getAccumulator();
            }else{
                turn = 1;
                pcb2.setPc(pc);
                pcb2.setAccumulator(accumulator);
                pcb2.setExtendedBit(extendedBit);
                pcb2.setMar(mar);
                pc = pcb1.getPc();
                mar = pcb1.getMar();
                extendedBit = pcb1.getExtendedBit();
                accumulator = pcb1.getAccumulator();
            }
            
            count = 3;
        }
        count--;
        
    }
    public void performSwitching(){
        if(completedProcesses<2){
            currentTurn();
        switch(turn){
            case 1:
                performFetchCycle(instructionSet1);
                break;
            case 2:
                performFetchCycle(instructionSet2);
                break;
        }
        }else{
            System.out.println("Processes completed, Exiting");
            for(int i=0;i<memory.length;i++){
                System.out.println(i+" : "+memory[i]);
            }
            
            System.exit(0);
        }
        
        
    }
    public void performFetchCycle(ArrayList<Instruction> instructionSet){
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
        }else{
            if(isSingleProcess){
                System.out.println("Processes completed, Exiting");
            for(int i=0;i<memory.length;i++){
                System.out.println(i+" : "+memory[i]);
            }
            
            System.exit(0);
            }
            completedProcesses++;
            count = 0;
            performSwitching();
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
        pc = mbr.getAddress();
        f = 0;
        performNextCycle();                
    }
    public void performBSA(){
        
        mar = mbr.getAddress();
        int temp = pc;
        pc = mbr.getAddress();
        mbr.setAddress(temp);
    }
    public void performISZ(){
        
    }
    public void performRef(){
        int address;
        address = mbr.getAddress();
        
        switch(address){
            case 800:
                accumulator = 0;
                break;
            case 400:
                extendedBit = 0;
                break;
            case 200:
                accumulator = ~accumulator;
                break;
            case 100:
                extendedBit = ~extendedBit;
                break;
            case 80:
                accumulator = accumulator/2;
                break;
            case 40:
                accumulator = accumulator*2;
                break;
            case 20:
                accumulator+=1;
                break;
            case 10:
                if(accumulator>0){
                    pc+=1;
                }
                break;
            case 8:
                if(accumulator<0){
                    pc+=1;
                }
                break;
            case 4:
                if(accumulator == 0){
                    pc+=1;
                }
                break;
            case 2:
                if(extendedBit == 0){
                    pc+=1;
                }
                break;
            case 1:
                System.exit(0);
                break;
                    
                    
                    
                                
        }
        
    }
    public void performNextCycle(){
        if(f==0 && r==0){
            if(isSingleProcess){
                performFetchCycle(instructionSet1);
            }
            performSwitching();
        }else if(f==0 && r==1){
            performIndirectCycle();
        }else if(f==1 && r==0){
            performExecuteCycle();
        }
    }
}
