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
    boolean processCompleted = false, isSingleProcess = true, didJump = false;
    int mar,count = 3,turn=0;
    int f = 0,temp = 0;
    int r = 0;
    ArrayList<ProcessBlock> currentProcesses;
    Loader ld1,ld2;
    int[] memory = {5,2,0,20,30,0,0,0,90,100,110,120,130,150};
    public static void main(String args[]){
//       String filePath ="src/contents.txt";
//        args[0] = "src/contents.txt";
//        args[1] = "src/contents2.txt";
        
        //String filePath="src/contents.txt", filePath2="src/contents2.txt";
        Run r = new Run();
        r.initializeInstructions(args);
         
//        if(args.length>1){
//            filePath = args[0];
//            filePath2 = args[1]; 
//            r.initializeInstructions(filePath,filePath2);
//        }else{
//            filePath = args[0];
//            r.initializeInstructions(filePath);
//        }
        
       
       
        
//        for(int i =0;i<instructionSet.size();i++){
//            System.out.println(instructionSet.get(i).getDirectBit());
//            System.out.println(instructionSet.get(i).getAddress());
//            System.out.println(instructionSet.get(i).getOpCode());
//        }
    }
    public void initializeInstructions(String args[]){
        currentProcesses = new ArrayList<>();
        isSingleProcess = false;
        for(int i =0;i<args.length;i++){
            ld1 = new Loader(args[i]);
            currentProcesses.add(new ProcessBlock(ld1.getInstructionSet(ld1.getFileReader()), new ProcessControlBlock(pc, mar, accumulator, extendedBit)));
        }
        performSwitching();
    }
    public void currentTurn(){
        if(count<=0){
            if(turn >= currentProcesses.size()){
                turn = 0;
            }else if(processCompleted){
                turn = turn;
                processCompleted = false;
            }else {
                saveState();
                turn = (turn+1)%currentProcesses.size();
                getState();
            }
            
            
            count = 3;
        }
        count--;
        
    }
    public void performSwitching(){
        if(completedProcesses<2){
            currentTurn();
            System.out.println("switched to process-"+turn);
            performFetchCycle(currentProcesses.get(turn).getInstructionSet());
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
            if(didJump){
                mar = pc;
                mbr = instructionSet.get(mar);
               
            }else{
                mar = pc;
                mbr = instructionSet.get(mar);
                  
            }
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
            completedProcesses++;
            currentProcesses.remove(turn);
            processCompleted = true;
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
        System.out.println("Performaing AND instruction : Process-"+turn);
        mar = mbr.getAddress();
        mbr.setAddress(memory[mar]);
        accumulator = accumulator & mbr.getAddress();
        f = 0;
        performNextCycle();
    }
    public void performADD(){
        System.out.println("Performaing ADD instruction : Process-"+turn);
        mar = mbr.getAddress();
       // mbr.setAddress(memory[mar]);
       // accumulator = accumulator + mbr.getAddress();
        accumulator+=memory[mar];
        f = 0;
        performNextCycle();
    }
    public void performLDA(){
        System.out.println("Performaing LDA instruction : Process-"+turn);
        mar = mbr.getAddress();
//        mbr.setAddress(memory[mar]);
//        accumulator = mbr.getAddress();
        accumulator = memory[mar];
        f = 0;
        performNextCycle();
    }
    public void performSTA(){
        System.out.println("Performaing STA instruction : Process-"+turn);
        mar = mbr.getAddress();
        //mbr.setAddress(accumulator);
       // memory[mar] = mbr.getAddress();
        memory[mar] = accumulator;
        f = 0;
        performNextCycle();
    }
    public void performBUN(){
        System.out.println("Performaing BUN instruction : Process-"+turn);
        if(temp>0){
            mbr.setAddress(temp);
        }
        pc = mbr.getAddress();
        f = 0;
        performNextCycle();                
    }
    public void performBSA(){
        System.out.println("Performaing BSA instruction : Process-"+turn);
        mar = mbr.getAddress();
        temp = pc;
        pc = mbr.getAddress();
        didJump = true;
        f = 0;
        performNextCycle();
        
    }
    public void performISZ(){
        mar = mbr.getAddress();
        mbr.setAddress(memory[mar]);
        mbr.setAddress(mbr.getAddress()+1);
        memory[mar] = mbr.getAddress();
        if(mbr.getAddress() == 0){
            pc+=1;
        }
        f = 0;
        performNextCycle();
    }
    public void performRef(){
        System.out.println("Register reference instruction : Process-"+turn);
        int address;
        address = mbr.getAddress();
        
        switch(address){
            case 800:
                System.out.println("Register reference instruction claer accumulator : Process-"+turn);
                accumulator = 0;
                f = 0;
                performNextCycle();
                break;
            case 400:
                System.out.println("Register reference instruction clear e bit : Process-"+turn);
                extendedBit = 0;
                f = 0;
                performNextCycle();
                break;
            case 200:
                System.out.println("Register reference instruction complement accumulator : Process-"+turn);
                accumulator = -accumulator;
                f = 0;
                performNextCycle();
                break;
            case 100:
                System.out.println("Register reference instruction complement e bit : Process-"+turn);
                extendedBit = -extendedBit;
                f = 0;
                performNextCycle();
                break;
            case 80:
                System.out.println("Register reference instruction right shift: Process-"+turn);
                accumulator = accumulator/2;
                f = 0;
                performNextCycle();
                break;
            case 40:
                System.out.println("Register reference instruction left shift: Process-"+turn);
                accumulator = accumulator*2;
                f = 0;
                performNextCycle();
                break;
            case 20:
                System.out.println("Register reference instruction INC accumulator: Process-"+turn);
                accumulator+=1;
                f = 0;
                performNextCycle();
                break;
            case 10:
                System.out.println("Register reference instruction INC on positive acc : Process-"+turn);
                if(accumulator>0){
                    pc+=1;
                }
                f = 0;
                performNextCycle();
                break;
            case 8:
                System.out.println("Register reference instruction INC on negative acc : Process-"+turn);
                if(accumulator<0){
                    pc+=1;
                }
                f = 0;
                performNextCycle();
                break;
            case 4:
                System.out.println("Register reference instruction skip on acc zero: Process-"+turn);
                if(accumulator == 0){
                    pc+=1;
                }
                f = 0;
                performNextCycle();
                break;
            case 2:
                System.out.println("Register reference instruction INC on e bit zero: Process-"+turn);
                if(extendedBit == 0){
                    pc+=1;
                }
                f = 0;
                performNextCycle();
                break;
            case 1:
                for(int i = 0;i<memory.length;i++){
                    System.out.println(memory[i]);
                }
                if(currentProcesses.size()>1){
                    f = 0;
                    completedProcesses++;
                    currentProcesses.remove(turn);
                    processCompleted = true;
                    count = 0;
                    performSwitching();
                }else{
                    System.exit(0);
                }
                
                break;
                    
                    
                    
                                
        }
        
    }
    public void performNextCycle(){
        if(f==0 && r==0){
            performSwitching();
        }else if(f==0 && r==1){
            performIndirectCycle();
        }else if(f==1 && r==0){
            performExecuteCycle();
        }
    }
    public void saveState(){
        ProcessBlock b = currentProcesses.get(turn);
            b.pcb.setPc(pc);
            b.pcb.setMar(mar);
            b.pcb.setExtendedBit(extendedBit);
            b.pcb.setAccumulator(accumulator);
    }
    public void getState(){
        ProcessBlock b = currentProcesses.get(turn);
            pc = b.pcb.getPc();
            mar = b.pcb.getMar();
            extendedBit = b.pcb.getExtendedBit();
            accumulator = b.pcb.getAccumulator();
    }
}
