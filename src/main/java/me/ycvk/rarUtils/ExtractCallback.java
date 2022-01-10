package me.ycvk.rarUtils;

import net.sf.sevenzipjbinding.*;

import java.io.*;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * @author VERO
 * @version 1.0
 * @date 2022/1/6/22:17
 */
public class ExtractCallback implements IArchiveExtractCallback {
    private int index;
    private String packageName;
    private IInArchive inArchive;
    private String ourDir;

    public ExtractCallback(IInArchive inArchive, String packageName, String ourDir) {
        this.inArchive = inArchive;
        this.packageName = packageName;
        this.ourDir = ourDir;
    }

    @Override
    public void setCompleted(long arg0) {
    }

    @Override
    public void setTotal(long arg0) {
    }

    @Override
    public ISequentialOutStream getStream(int index, ExtractAskMode extractAskMode) throws SevenZipException {
        this.index = index;
        final String path = (String) inArchive.getProperty(index, PropID.PATH);
        System.out.println("path === >" + path);
        //如果是文件夹，则不需要解压
        final boolean isFolder = (boolean) inArchive.getProperty(index, PropID.IS_FOLDER);
        final String[] oldPath = {""};
        return data -> {
            try {
                if (!isFolder) {
                    System.out.println(path);
                    File file = new File(ourDir + File.separator + path);
                    save2File(file, data, path.equals(oldPath[0]));
                    oldPath[0] = path;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return data.length;
        };
    }

    @Override
    public void prepareOperation(ExtractAskMode arg0) {
    }

    @Override
    public void setOperationResult(ExtractOperationResult extractOperationResult) throws SevenZipException {
        String path = (String) inArchive.getProperty(index, PropID.PATH);
        boolean isFolder = (boolean) inArchive.getProperty(index, PropID.IS_FOLDER);
//        if(ZipUtils.checkOnlyGetDir(path) && !isFolder){
//            if (extractOperationResult != ExtractOperationResult.OK) {
//                StringBuilder sb = new StringBuilder();
//                sb.append("解压").append(packageName).append("包的").append(path).append("文件");
//                sb.append("失败！");
//                log.error(sb.toString());
//            }
//        }
        if (!isFolder) {
            if (extractOperationResult != ExtractOperationResult.OK) {
                StringBuilder sb = new StringBuilder();
                sb.append("解压").append(packageName).append("包的").append(path).append("文件");
                sb.append("失败！");
                System.out.println(sb);
            }
        }
    }

    public static boolean save2File(File file, byte[] msg, boolean append) {
        BufferedOutputStream bos = null;
        try {
            File parent = file.getParentFile();
            boolean bool;
            if ((!parent.exists()) && (!parent.mkdirs())) {
                return false;
            }
            //append为true表示追加
            bos = new BufferedOutputStream(new FileOutputStream(file, append));
            bos.write(msg);
            bos.flush();
            return true;
        } catch (FileNotFoundException e) {
            return false;
        } catch (IOException e) {
            File parent;
            return false;
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException ignored) {
                }
            }
        }
    }

}
