/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domain;

import Utilities.ArrayUtils;

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
    private int DATA_SIZE_START_INDEX;
    private int DATA_START_INDEX;

    // RIFF CHUNK
    private byte[] riffChunk;
    private int sizeInHeader;
    private char[] format; // Should be WAVE

    // FMT SUBCHUNK
    private byte[] fmtChunk;
    private char[] fmtSubChunkId;
    private int fmtSubChunkSize;
    private int audioFormat; // 2 bytes
    private int numChannels; //  2 bytes
    private int sampleRate;
    private int byteRate;
    private int blockAlign; // 2 bytes
    private int bitsPerSample; // 2 bytes

    // DATA SUBCHUNK
    private byte[] dataHeader;
    private int dataSize;
    private byte[] data;

    /**
     * Creates a WavFile which can be used for storing and manipulating Wav
     * Data.
     *
     * The data is generally comprises three parts: the header, the fmt
     * subchunk, and the data subchunk.
     *
     * @param bytes Byte array containing the complete byte representation of a
     * Wav file.
     */
    public WavFile(byte[] bytes) {
        try {
            this.isolateRiff(bytes);
            this.isolateFmt(bytes);
            this.calculateNonfixedIndexes();
            // FMT chunk here, because its length was not known for sure previously
            this.fmtChunk = ArrayUtils.slice(bytes, FMT_START_INDEX, DATA_CHUNK_START_INDEX);
            this.isolateData(bytes);

        } catch (Exception e) {
            System.out.println(e);
            System.out.println("ERROR ERROR ");
        }

    }

    private void isolateRiff(byte[] bytes) {
        this.riffChunk = ArrayUtils.slice(bytes, 0, 12);
        this.sizeInHeader = this.littleEndianBytesToInteger(ArrayUtils.slice(this.riffChunk, RIFF_CHUNK_SIZE, RIFF_CHUNK_SIZE + 4));
        this.format = this.bigEndianBytesToChars(ArrayUtils.slice(this.riffChunk, FORMAT_INDEX, FORMAT_INDEX + 4));
        // TODO: validateRiff()
    }

    private void isolateFmt(byte[] bytes) {
        this.fmtSubChunkId = this.bigEndianBytesToChars(ArrayUtils.slice(bytes, FMT_START_INDEX, FMT_START_INDEX + 4));
        this.fmtSubChunkSize = this.littleEndianBytesToInteger(ArrayUtils.slice(bytes, FMT_SIZE_START_INDEX, FMT_SIZE_START_INDEX + 4));
        this.audioFormat = this.littleEndianBytesToInteger(ArrayUtils.slice(bytes, AUDIO_FORMAT_INDEX, AUDIO_FORMAT_INDEX + 2));
        this.numChannels = this.littleEndianBytesToInteger(ArrayUtils.slice(bytes, NUM_CHANNELS_START_INDEX, NUM_CHANNELS_START_INDEX + 2));
        this.sampleRate = this.littleEndianBytesToInteger(ArrayUtils.slice(bytes, SAMPLE_RATE_START_INDEX, SAMPLE_RATE_START_INDEX + 4));
        this.byteRate = this.littleEndianBytesToInteger(ArrayUtils.slice(bytes, BYTE_RATE_START_INDEX, BYTE_RATE_START_INDEX + 4));
        this.blockAlign = this.littleEndianBytesToInteger(ArrayUtils.slice(bytes, BLOCK_ALIGN_START_INDEX, BLOCK_ALIGN_START_INDEX + 2));
        this.bitsPerSample = this.littleEndianBytesToInteger(ArrayUtils.slice(bytes, BPS_START_INDEX, BPS_START_INDEX + 2));
    }

    private void calculateNonfixedIndexes() {
        this.DATA_CHUNK_START_INDEX = this.fmtSubChunkSize + this.FMT_SIZE_START_INDEX + 4;
        this.DATA_SIZE_START_INDEX = this.DATA_CHUNK_START_INDEX + 4;
        this.DATA_START_INDEX = this.DATA_SIZE_START_INDEX + 4;
    }

    private void isolateData(byte[] bytes) {
        // DATA SUBCHUNK
        this.dataHeader = ArrayUtils.slice(bytes, this.DATA_CHUNK_START_INDEX, this.DATA_START_INDEX);
        this.dataSize = this.littleEndianBytesToInteger(ArrayUtils.slice(bytes, this.DATA_SIZE_START_INDEX, this.DATA_SIZE_START_INDEX + 4));
        this.data = ArrayUtils.slice(bytes, this.DATA_START_INDEX, bytes.length);
    }

    /**
     * Sets the audio data, i.e. data subchunk excluding subchunk id and
     * subchunk size.
     *
     * @param data
     */
    public void setAudioData(byte[] data) {
        this.data = data;
    }

    /**
     * Returns the header of the wav file, i.e. bytes 0-11.
     *
     * @return byte[12] containing the first 12 bytes of the file.
     */
    public byte[] getHeaderChunk() {
        return this.riffChunk;
    }

    /**
     * Gets the size of the data subchunk, including data, subchunk id, and
     * subchunk size.
     *
     * @return Size of the entire data subchunk.
     */
    public int getDataChunkSize() {
        return this.data.length + this.dataHeader.length;
    }

    /**
     *
     * @return File size as given in the header (bytes 4-8 of the file).
     */
    public int getSizeInHeader() {
        return this.sizeInHeader;
    }

    /**
     * Returns the audio format of the file, usually value 1. if the value is
     * other than 1, the file is probably compressed and this program can't
     * handle it.
     *
     * @return Audio format of the file as Integer.
     */
    public int getAudioFormat() {
        return this.audioFormat;
    }

    /**
     * @return Number of channels in the file. Typically 2.
     */
    public int getNumberOfChannels() {
        return this.numChannels;
    }

    /**
     * Returns the sample rate of the file, e.g. 44100, 92000, 16000.
     *
     * @return Sample rate as Hz.
     */
    public int getSampleRate() {
        return this.sampleRate;
    }

    /**
     *
     * @return Byte rate as bytes per second.
     */
    public int getByteRate() {
        return this.byteRate;
    }

    public int getBlockAlign() {
        return this.blockAlign;
    }

    /**
     * Returns the number of bits per each sample, e.g. 8, 16, 32.
     *
     * @return Bits per sample.
     */
    public int getBitsPerSample() {
        return this.bitsPerSample;
    }

    /**
     * Returns the index at which the audio data begins, i.e. after the data
     * subchunk id and data subchunk size.
     *
     * @return Index at which audio data begins.
     */
    public int getAudioStartingIndex() {
        return this.DATA_START_INDEX;
    }

    /**
     *
     * @return the size of the audio data as given in the subchunk size field of
     * the data subchunk.
     */
    public int getDataSize() {
        return this.dataSize;
    }

    /**
     * Gets the data subchunk without chunk id and chunk size, i.e. only the
     * audio bytes.
     *
     * @return byte[] containing all audio bytes of the file
     */
    public byte[] getAudioData() {
        return this.data;
    }

    /**
     * Creates a byte array which can be used as-is for writing the file to
     * disk. If the data has not been modified, this method will return a byte
     * array identical to the input given to the constructor.
     *
     * @return byte[] containing the entire file
     */
    public byte[] toSaveableByteArray() {
        byte[] ret = ArrayUtils.append(this.riffChunk, this.fmtChunk);
        ret = ArrayUtils.append(ret, this.dataHeader);
        ret = ArrayUtils.append(ret, this.data);
        return ret;
    }

    /**
     * Gets file size as described in the file header
     *
     * @return File size - 8
     */
    public int getDataSizeInDataHeader() {
        return littleEndianBytesToInteger(ArrayUtils.slice(this.dataHeader, 4, 8));
    }

    /**
     * Returns the audio bytes for one channel as indicated by the parameter
     * num. The byte data returned is raw, i.e. it contains the bytes in the
     * order they appear in the audio file. If the audio is 16-bit PCM, which is
     * little endian, the bytes will need to be processed separately to bring
     * them in the desired order for type conversions.
     *
     * @param num The channel from which to extract audio data.
     * @return raw bytes of the audio channel.
     */
    public byte[] getChannelByNumber(int num) {
        if (num > this.numChannels || num < 0) {
            throw new IllegalArgumentException("");
        }
        byte[] channelBytes = new byte[this.data.length / this.numChannels];

        int numOfBytesInSample = this.bitsPerSample / 8;
        int startingIndex = (num - 1) * numOfBytesInSample;
        //int startingIndex = (num) * numOfBytesInSample;
        int locationInTargetArray = 0;
        for (int i = startingIndex; i < this.data.length; i += this.blockAlign) {
            for (int l = 0; l < numOfBytesInSample && (i + l) < this.data.length && locationInTargetArray < channelBytes.length; l++) {
                channelBytes[locationInTargetArray] = this.data[i + l];
                locationInTargetArray++;
            }
        }
        return channelBytes;
    }

    public void setChannelByNumber(int num, byte[] source) {
        if (num > this.numChannels || num < 0) {
            throw new IllegalArgumentException("");
        }

        int numOfBytesInSample = this.bitsPerSample / 8;
        int startingIndex = (num - 1) * numOfBytesInSample;
        //int startingIndex = (num) * numOfBytesInSample;
        int locationInSourceArray = 0;
        for (int i = startingIndex; i < this.data.length; i += this.blockAlign) {
            for (int l = 0; l < numOfBytesInSample; l++) {
                this.data[i + l] = source[locationInSourceArray];
                locationInSourceArray++;
            }
        }
    }

    /**
     *
     * @return
     */
    public String getSummary() {
        String summary = "SIZE - in header: " + this.sizeInHeader
                + "\nSecond subchunk size: " + this.fmtSubChunkSize
                + "\nAudio Format: " + this.audioFormat
                + "\nNumber of Channels: " + this.numChannels
                + "\nSample rate: " + this.sampleRate
                + "\nByte rate: " + this.byteRate + ", " + ((this.sampleRate * this.numChannels * this.bitsPerSample / 8))
                + "\nBlock Align: " + this.blockAlign + ", " + (this.numChannels * this.bitsPerSample / 8)
                + "\nBits per sample: " + this.bitsPerSample
                + "\nData chunk start index: " + (DATA_CHUNK_START_INDEX)
                + "\nData size: " + this.dataSize
                + "\nLength of this.data: " + this.data.length;
        return summary;
    }

    /**
     * Creates an integer value our of of 1-4 bytes that form a little-endian
     * word.
     *
     * @param bytes little-endian word as bytes
     * @return Integer Integer value of the word
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
    }

}
