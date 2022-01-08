package me.ycvk.codetest;

import net.sf.sevenzipjbinding.IInArchive;
import net.sf.sevenzipjbinding.SevenZip;
import net.sf.sevenzipjbinding.SevenZipException;
import net.sf.sevenzipjbinding.impl.RandomAccessFileInStream;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
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
public class RarDemo {
    public static void main(String[] args) throws IOException {
//        String rarDir = "D:\\TEMP\\A.rar";
//        String outDir = "D:\\TEMP\\rar5";
        decompression(args[0], args[1]);
    }

    public static void decompression(String rarDir, String outDir) throws SevenZipException, FileNotFoundException {
        // 第一个参数是需要解压的压缩包路径，第二个参数参考JdkAPI文档的RandomAccessFile
        RandomAccessFile randomAccessFile = new RandomAccessFile(rarDir, "r");
        IInArchive inArchive = SevenZip.openInArchive(null, new RandomAccessFileInStream(randomAccessFile));

        //定义一个数组，长度等于压缩包中的文件数量
        int[] in = IntStream.range(0, inArchive.getNumberOfItems()).toArray();
        inArchive.extract(in, false, new ExtractCallback(inArchive, "", outDir));

    }
}
