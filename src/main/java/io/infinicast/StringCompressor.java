package io.infinicast;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * Created by ASG on 10.09.2015.
 */
public class StringCompressor {

	public static byte[] compress(String data) throws IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream(data.length());
		GZIPOutputStream gzip = new GZIPOutputStream(bos);
		gzip.write(data.getBytes(StandardCharsets.UTF_8));
		gzip.close();
		byte[] compressed = bos.toByteArray();
		bos.close();
		return compressed;
	}

	public static String decompress(byte[] compressed) throws IOException {
		ByteArrayInputStream bis = new ByteArrayInputStream(compressed);
		GZIPInputStream gis = new GZIPInputStream(bis);

		byte[] outBuffer=new byte[1024];
		ByteArrayOutputStream bos=new ByteArrayOutputStream();
		int len;
		while ((len=gis.read(outBuffer,0,outBuffer.length)) != -1) {
			bos.write(outBuffer,0,len);
		}

		String result = new String( bos.toByteArray(),StandardCharsets.UTF_8);

/*		BufferedReader br = new BufferedReader(new InputStreamReader(gis, "UTF-8"));
		StringBuilder sb = new StringBuilder();
		String line;
		while((line = br.readLine()) != null) {
			sb.append(line);
		}*/
		//br.close();
		gis.close();
		bis.close();
		//return sb.toString();
		return result;
	}
}
