
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import domain.WavFile;
import IO.IOManager;
import static org.junit.Assert.*;

public class WavFileTest {

    private byte[] smallTestFile; // 8 kHZ file, i.e. small

    public WavFileTest() throws IOException {
        smallTestFile = IOManager.readFileToBytes("../samples/8kHz.wav");
        //testFile = Files.readAllBytes(Paths.get("./8kHz.wav"));
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void wavFileCreatedWithoutCrashing() {
        WavFile wav = new WavFile(smallTestFile);
        assertNotNull(wav);
    }

    @Test
    public void wavFileSummaryIsValid() {
        String testString = "SIZE - in header: 1738008"
                + "\nSecond subchunk size: 16"
                + "\nAudio Format: 1"
                + "\nNumber of Channels: 2"
                + "\nSample rate: 8000"
                + "\nByte rate: 32000, 32000"
                + "\nBlock Align: 4, 4"
                + "\nBits per sample: 16"
                + "\nData chunk start index: 36"
                + "\nData size: 1737972"
                + "\nLength of this.data: 1737972";
        WavFile wav = new WavFile(smallTestFile);
        assertEquals(wav.getSummary(), testString);
    }

    @Test
    public void methodsReturnValidValuesForSmallFile() {
        WavFile wav = new WavFile(smallTestFile);

        assertEquals(1, wav.getAudioFormat());
        assertEquals(2, wav.getNumberOfChannels());
        assertEquals(8000, wav.getSampleRate());
        assertEquals(16, wav.getBitsPerSample());
    }

    @Test
    public void sizesAreValidForSmallFile() {
        WavFile wav = new WavFile(smallTestFile);

        assertEquals(1738008, wav.getSizeInHeader());
        assertEquals(1737972, wav.getDataSize());
        assertEquals(1737980, wav.getDataChunkSize());
        assertEquals(4, wav.getBlockAlign());
        assertEquals(1737972, wav.getDataSizeInDataHeader());
    }

    @Test
    public void indexesAreValidForSmallFile() {
        WavFile wav = new WavFile(smallTestFile);

        assertEquals(44, wav.getAudioStartingIndex());
    }

    @Test
    public void returnsIdenticalArrayForSmallFile() {
        WavFile wav = new WavFile(smallTestFile);
        assertArrayEquals(smallTestFile, wav.toSaveableByteArray());
    }

    @Test
    public void gettingChannelBytesReturnsReasonableValues() {
        byte[] testData = new byte[40];
        WavFile wav = new WavFile(smallTestFile);
        for (int i = 0; i < testData.length; i += 4) {
            testData[i] = 4;
            testData[i + 1] = 0;
            testData[i + 2] = 8;
            testData[i + 3] = 0;
        }

        wav.setAudioData(testData);
        byte[] firstChannelData = wav.getChannelByNumber(1);
        byte[] secondChannelData = wav.getChannelByNumber(2);

        for (int i = 0; i < 20; i += 2) {
            assertEquals(firstChannelData[i], 4);
            assertEquals(firstChannelData[i + 1], 0);
            assertEquals(secondChannelData[i], 8);
            assertEquals(secondChannelData[i + 1], 0);
        }
    }

}
