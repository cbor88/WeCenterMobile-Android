package cn.fanfan.common;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.os.Environment;
import android.util.Log;

public class CompressAvata {
	String compressAvatarPath;

	public CompressAvata(String avatarPath) {
		BitmapFactory.Options newOpts = new BitmapFactory.Options();
		newOpts.inJustDecodeBounds = true;
		Bitmap bitmap = BitmapFactory.decodeFile(avatarPath, newOpts);
		newOpts.inJustDecodeBounds = false;
		int w = newOpts.outWidth;
		int h = newOpts.outHeight;
		float hh = 400f;
		float ww = 240f;
		int be = 8;// be=1��ʾ������
		// // if (w > h && w > ww) {// �����ȴ�Ļ����ݿ�ȹ̶���С����
		// // be = (int) (newOpts.outWidth / ww);
		// // } else if (w < h && h > hh) {// ����߶ȸߵĻ����ݿ�ȹ̶���С����
		// // be = (int) (newOpts.outHeight / hh);
		// // }
		// if (be <= 0)
		// be = 1;
		newOpts.inSampleSize = be;// �������ű���
		bitmap = BitmapFactory.decodeFile(avatarPath, newOpts);
		Bitmap Cbitmap = compressByCutQuality(bitmap);// ѹ���ñ�����С���ٽ�������ѹ��
		String path = Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED) ? Environment
				.getExternalStorageDirectory().getPath() + "/fanfan" : null
				+ "/fanfan";
		File foldFile = new File(path);
		if (!foldFile.exists()) {
			foldFile.mkdir();
		}
		
		compressAvatarPath = path + File.separator + "avatarImageMin.jpg";
		File file = new File(path + File.separator + "avatarImageMin.jpg");
		try {
			file.createNewFile();
			FileOutputStream outputStream = new FileOutputStream(file);
			bitmap.compress(CompressFormat.JPEG, 100, outputStream);
			outputStream.flush();
			outputStream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.i("����ѹ��", "ʧ��");
		}

		// bitmap����
	}

	public String getCompressAvatarPath() {
		return compressAvatarPath;
	}

	public Boolean isConformSize() {
		BitmapFactory.Options newOpts = new BitmapFactory.Options();
		newOpts.inJustDecodeBounds = true;
		Bitmap bitmap = BitmapFactory.decodeFile(compressAvatarPath, newOpts);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
		if (baos.toByteArray().length / 1024 > 512) {
			return true;
		} else {
			return false;
		}
	}

	private Bitmap compressByCutQuality(Bitmap image) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.JPEG, 90, baos);// ����ѹ������������100��ʾ��ѹ������ѹ��������ݴ�ŵ�baos��
		int options = 80;
		while (baos.toByteArray().length / 1024 > 512) { // ѭ���ж����ѹ����ͼƬ�Ƿ����512kb,���ڼ���ѹ��
			baos.reset();// ����baos�����baos
			image.compress(Bitmap.CompressFormat.JPEG, options, baos);// ����ѹ��options%����ѹ��������ݴ�ŵ�baos��
			options -= 10;// ÿ�ζ�����10
		}
		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// ��ѹ���������baos��ŵ�ByteArrayInputStream��
		Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// ��ByteArrayInputStream��������ͼƬ
		return bitmap;

	}
}
