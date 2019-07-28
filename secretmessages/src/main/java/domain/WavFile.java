/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domain;

import IO.IOManager;
import Utilities.ArrayUtils;
import java.io.IOException;

/**
 *
 * @author joonas
 */
public class WavFile {

    private final int RIFF_INDEX = 0; // BIG
    private final int RIFF_CHUNK_SIZE = 4;
    private final int FORMAT_INDEX = 8; // BIG
    private final int FMT_START_INDEX = 12; // BIG
    private final int FMT_SIZE_START_INDEX = 16;
    private final int AUDIO_FORMAT_INDEX = 20;
    private final int NUM_CHANNELS_START_INDEX = 22;
    private final int SAMPLE_RATE_START_INDEX = 24;
    private final int BYTE_RATE_START_INDEX = 28;
    private final int BLOCK_ALIGN_START_INDEX = 32;
    private final int BPS_START_INDEX = 34;
    private int DATA_CHUNK_START_INDEX;
    private int DATA_START_INDEX;

    private byte[] riffChunk;
    private int sizeInHeader;
    private char[] format; // Should be WAVE

    private byte[] fmtChunk;
    private char[] fmtSubChunkId;
    private int fmtSubChunkSize;
    private int audioFormat; // 2 bytes
    private int numChannels; //  2 bytes
    private int sampleRate;
    private int byteRate;
    private int blockAlign; // 2 bytes
    private int bitsPerSample; // 2 bytes

    private byte[] dataHeader;

    private byte[] data;

    public WavFile(String location) {
        try {
            byte[] bytes = IOManager.readFileToBytes(location);
            this.riffChunk = ArrayUtils.slice(bytes, 0, 12);
            this.sizeInHeader = this.littleEndianBytesToInteger(ArrayUtils.slice(this.riffChunk, RIFF_CHUNK_SIZE, RIFF_CHUNK_SIZE + 4));
            this.format = this.bigEndianBytesToChars(ArrayUtils.slice(this.riffChunk, FORMAT_INDEX, FORMAT_INDEX + 4));

            this.fmtSubChunkId = this.bigEndianBytesToChars(ArrayUtils.slice(bytes, FMT_START_INDEX, FMT_START_INDEX + 4));
            this.fmtSubChunkSize = this.littleEndianBytesToInteger(ArrayUtils.slice(bytes, FMT_SIZE_START_INDEX, FMT_SIZE_START_INDEX + 4));
            this.audioFormat = this.littleEndianBytesToInteger(ArrayUtils.slice(bytes, AUDIO_FORMAT_INDEX, AUDIO_FORMAT_INDEX + 2));
            this.numChannels = this.littleEndianBytesToInteger(ArrayUtils.slice(bytes, NUM_CHANNELS_START_INDEX, NUM_CHANNELS_START_INDEX + 2));
            this.sampleRate = this.littleEndianBytesToInteger(ArrayUtils.slice(bytes, SAMPLE_RATE_START_INDEX, SAMPLE_RATE_START_INDEX + 4));
            this.byteRate = this.littleEndianBytesToInteger(ArrayUtils.slice(bytes, BYTE_RATE_START_INDEX, BYTE_RATE_START_INDEX + 4));
            this.blockAlign = this.littleEndianBytesToInteger(ArrayUtils.slice(bytes, BLOCK_ALIGN_START_INDEX, BLOCK_ALIGN_START_INDEX + 2));
            this.bitsPerSample = this.littleEndianBytesToInteger(ArrayUtils.slice(bytes, BPS_START_INDEX, BPS_START_INDEX + 2));

            this.DATA_CHUNK_START_INDEX = this.fmtSubChunkSize + this.FMT_SIZE_START_INDEX + 4;
            this.fmtChunk = ArrayUtils.slice(bytes, FMT_START_INDEX, DATA_CHUNK_START_INDEX);
            this.DATA_START_INDEX = this.DATA_CHUNK_START_INDEX + 4;
            System.out.println("Data chunk start index: " + (DATA_CHUNK_START_INDEX));

            System.out.println("SIZE - whole file: " + bytes.length);
            System.out.println("SIZE - in header: " + this.sizeInHeader);
            System.out.println("Second subchunk size: " + this.fmtSubChunkSize);
            System.out.println("Audio Format: " + this.audioFormat);
            System.out.println("Number of Channels: " + this.numChannels);
            System.out.println("Sample rate: " + this.sampleRate);
            System.out.println("Byte rate: " + this.byteRate + ", " + (this.sampleRate * this.numChannels * this.bitsPerSample / 8));
            System.out.println("Block Align: " + this.blockAlign + ", " + (this.numChannels * this.bitsPerSample / 8));
            System.out.println("Bits per sample: " + this.bitsPerSample);

            System.out.println("Data starting bytes: ");
            for (int i = this.DATA_CHUNK_START_INDEX; i < this.DATA_START_INDEX; i++) {
                System.out.print((char) (bytes[i]));
            }
            System.out.println("");

        } catch (IOException e) {
            System.out.println(e);
            System.exit(0);
        }

    }

    public byte[] getData() {
        return this.data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    /*public byte[] getFormatChunk() {
        return this.fmt;
    }*/
    public byte[] getHeaderChunk() {
        return this.riffChunk;
    }

    public int getDataChunkSize() {
        return this.data.length + this.dataHeader.length;
    }

    public int getDataSize() {
        return this.data.length;
    }

    public int getSizeInheader() {
        return this.sizeInHeader;
    }

    /**
     *
     * @return
     */
    public int getProscribedDataSize() {
        return littleEndianBytesToInteger(ArrayUtils.slice(this.dataHeader, 4, 8));
    }

    /**
     *
     * @param bytes
     * @return
     */
    private Integer littleEndianBytesToInteger(byte[] bytes) {
        if (bytes.length > 4) {
            throw new Error("Array too long!");
        }
        int integer = 0x00;
        int temp = 0x00;
        for (int i = bytes.length - 1; i >= 0; i--) {
            temp = (bytes[i] & 0xFF) << (8 * i);
            integer = integer | temp;
        }
        //int integer = (bytes[3] & 0xff) << 24 | (bytes[2] & 0xff) << 16 | (bytes[1] & 0xff) << 8 | bytes[0] & 0xff;
        return integer;
    }

    /**
     *
     * @param bytes
     * @return
     */
    private char[] bigEndianBytesToChars(byte[] bytes) {
        if (bytes.length < 4) {
            throw new Error("Array too short!");

        }
        char[] ret = new char[bytes.length];
        for (int i = 0; i < bytes.length; i++) {
            ret[i] = (char) (bytes[i]);
        }
        return ret;
        //int integer = (bytes[3] & 0xff) << 24 | (bytes[2] & 0xff) << 16 | (bytes[1] & 0xff) << 8 | bytes[0] & 0xff;
        //return integer;
    }

}
