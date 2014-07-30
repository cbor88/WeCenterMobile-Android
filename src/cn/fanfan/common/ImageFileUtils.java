package cn.fanfan.common;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.Environment;

/*
 * edit at 2014/7/11
 * saveBitmap(String fileName, Bitmap bitmap);����ͼƬ
 * getBitmap(String fileName)ȡͼƬ
 * isFileExists()�ж��ļ��Ƿ����
 * deleteFile()�������ͼƬ�ļ�����
 * deleteFile(String FileName)ɾ��ĳ��ͼƬ�ļ�
 */

public class ImageFileUtils {
	private static String sdRootPath = Environment
			.getExternalStorageDirectory().getPath();
	private static String dataRootPath = null;
	private final static String FOLDER_NAME = "/fanfan";

	public ImageFileUtils(Context context) {
		dataRootPath = context.getCacheDir().getPath();
	}

	private String getStorageDirectory() {
		return Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED) ? sdRootPath + FOLDER_NAME
				: dataRootPath + FOLDER_NAME;
	}

	public void saveBitmap(String fileName, Bitmap bitmap) throws IOException {
		fileName = fileName.replaceAll("[^\\w]", "");
		if (bitmap == null) {
			return;
		}
		String path = getStorageDirectory();
		File foldFile = new File(path);
		if (!foldFile.exists()) {
			foldFile.mkdir();
		}
		File file = new File(path + File.separator + fileName);
		file.createNewFile();
		FileOutputStream outputStream = new FileOutputStream(file);
		bitmap.compress(CompressFormat.JPEG, 80, outputStream);
		outputStream.flush();
		outputStream.close();
	}

	public Bitmap getBitmap(String fileName) {
		fileName = fileName.replaceAll("[^\\w]", "");
		return decodeSampledBitmapFromResource(getStorageDirectory()
				+ File.separator + fileName, 200, 150);
	}

	public Bitmap decodeSampledBitmapFromResource(String filepath,
			int reqWidth, int reqHeight) {
		// ��һ�ν�����inJustDecodeBounds����Ϊtrue������ȡͼƬ��С
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(filepath, options);
		// �������涨��ķ�������inSampleSizeֵ
		options.inSampleSize = calculateInSampleSize(options, reqWidth,
				reqHeight);
		// ʹ�û�ȡ����inSampleSizeֵ�ٴν���ͼƬ
		options.inJustDecodeBounds = false;
		Bitmap bitmap = BitmapFactory.decodeFile(filepath, options);
		return bitmap;
	}

	public static int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {
		// ԴͼƬ�ĸ߶ȺͿ��
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;
		if (height > reqHeight || width > reqWidth) {
			// �����ʵ�ʿ�ߺ�Ŀ���ߵı���
			final int heightRatio = Math.round((float) height
					/ (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);
			// ѡ���͸�����С�ı�����ΪinSampleSize��ֵ���������Ա�֤����ͼƬ�Ŀ�͸�
			// һ��������ڵ���Ŀ��Ŀ�͸�
			inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
		}
		return inSampleSize;
	}

	public boolean isFileExists(String fileName) {
		return new File(getStorageDirectory() + File.separator + fileName)
				.exists();
	}

	public long getFileSize(String fileName) {
		return new File(getStorageDirectory() + File.separator + fileName)
				.length();
	}

	public void deleteFile() {
		File dirFile = new File(getStorageDirectory());
		if (!dirFile.exists()) {
			return;
		}
		if (dirFile.isDirectory()) {
			String[] children = dirFile.list();
			for (int i = 0; i < children.length; i++) {
				new File(dirFile, children[i]).delete();
			}
		}
		dirFile.delete();
	}

	public void deleteFile(String fileName) {
		fileName = fileName.replaceAll("[^\\w]", "");
		File dirFile = new File(getStorageDirectory());
		if (!dirFile.exists()) {
			return;
		}
		File deleteFile = new File(getStorageDirectory() + File.separator
				+ fileName);
		if (!deleteFile.exists()) {
			return;
		} else {
			deleteFile.delete();
		}
	}
}
