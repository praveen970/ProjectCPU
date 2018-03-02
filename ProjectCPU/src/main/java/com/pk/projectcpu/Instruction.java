/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pk.projectcpu;

/**
 *
 * @author PraveenKumar
 */
public class Instruction {
    private int directBit;
    private int opCode;
    private int address;

    public Instruction(int directBit, int opCode, int address) {
        this.directBit = directBit;
        this.opCode = opCode;
        this.address = address;
    }

    public int getDirectBit() {
        return directBit;
    }

    public int getOpCode() {
        return opCode;
    }

    public int getAddress() {
        return address;
    }

    public void setDirectBit(int directBit) {
        this.directBit = directBit;
    }

    public void setOpCode(int opCode) {
        this.opCode = opCode;
    }

    public void setAddress(int address) {
        this.address = address;
    }
    
}
