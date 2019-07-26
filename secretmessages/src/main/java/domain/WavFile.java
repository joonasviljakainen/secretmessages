/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domain;

/**
 *
 * @author joonas
 */
public class WavFile {
    
    private byte[] header;
    private byte[] data;
    public WavFile(String location) {
        
    }
    
    public byte[] getData() {
        return this.data;
    }
    
    public void setData(byte[] data) {
        this.data = data;
        
    }
    
    
}
