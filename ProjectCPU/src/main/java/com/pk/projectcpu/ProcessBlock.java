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
public class ProcessBlock {
    ArrayList<Instruction> instructionSet;
    ProcessControlBlock pcb;

    public ProcessBlock(ArrayList<Instruction> instructionSet, ProcessControlBlock pcb) {
        this.instructionSet = instructionSet;
        this.pcb = pcb;
    }

    public ArrayList<Instruction> getInstructionSet() {
        return instructionSet;
    }

    public void setInstructionSet(ArrayList<Instruction> instructionSet) {
        this.instructionSet = instructionSet;
    }

    public ProcessControlBlock getPcb() {
        return pcb;
    }

    public void setPcb(ProcessControlBlock pcb) {
        this.pcb = pcb;
    }
    
}
