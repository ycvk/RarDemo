package me.ycvk.rarUtils;

import net.sf.sevenzipjbinding.IInArchive;
import net.sf.sevenzipjbinding.SevenZip;
import net.sf.sevenzipjbinding.SevenZipException;
import net.sf.sevenzipjbinding.impl.RandomAccessFileInStream;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Objects;
import java.util.stream.IntStream;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * @author VERO
 * @version 1.0
 * @date 2022/1/6/22:14
 */
public class RarApplication {

    private static final String FILE_SEPARATOR = System.getProperty("file.separator");

    public static String getRealFilePath(String path) {
        return path.replace("/", FILE_SEPARATOR).replace("\\", FILE_SEPARATOR);
    }

    public static void main(String[] args) {
        String rarDir = getRealFilePath(args[0]);
        String outDir = getRealFilePath(args[1]);
        decompression(rarDir, outDir);
    }

    public static void decompression(String rarDir, String outDir) {

        BufferedRandomAccessFile randomAccessFile = null;
        IInArchive inArchive = null;

        try {
            // 第一个参数是需要解压的压缩包路径，第二个参数参考JdkAPI文档的RandomAccessFile
            randomAccessFile = new BufferedRandomAccessFile(rarDir, "rw");
            inArchive = SevenZip.openInArchive(null, new RandomAccessFileInStream(randomAccessFile));

            //定义一个数组，长度等于压缩包中的文件数量
            int[] in = IntStream.range(0, inArchive.getNumberOfItems()).toArray();
            //解压缩
            inArchive.extract(in, false, new ExtractCallback(inArchive, "", outDir));

        } catch (FileNotFoundException e) {
            System.out.println(rarDir + "-FileNotFoundException occurs: ");
            e.printStackTrace();
        } catch (SevenZipException e) {
            System.out.println("SevenZipException occurs: ");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("IOException occurs: ");
            e.printStackTrace();
        } finally {
            try {
                Objects.requireNonNull(inArchive).close();
                Objects.requireNonNull(randomAccessFile).close();
            } catch (IOException e) {
                System.out.println("关闭randomAccessFile和IInArchive流时-IOException occurs: ");
                e.printStackTrace();
            }
        }

    }
}
