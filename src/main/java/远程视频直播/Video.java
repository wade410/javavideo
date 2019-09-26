package 远程视频直播;

import javax.swing.JFrame;

import org.bytedeco.ffmpeg.global.avcodec;
import org.bytedeco.javacpp.Loader;
import org.bytedeco.javacv.CanvasFrame;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.FrameRecorder;
import org.bytedeco.javacv.OpenCVFrameConverter;
import org.bytedeco.opencv.global.opencv_objdetect;
import org.bytedeco.opencv.opencv_core.IplImage;



public class Video {
//	public static void main(String[] args) throws Exception, InterruptedException, org.bytedeco.javacv.FrameRecorder.Exception {
//		recordCamera("D:/output.mp4",25);
//	}
	
	public static void main(String[] args) throws Exception, InterruptedException, org.bytedeco.javacv.FrameRecorder.Exception {
		recordCamera("rtmp://10.2.11.123:1935/live/app",25);//10.2.61.223这里要变成自己的ip
	}

	/**
	 * 按帧录制本机摄像头视频（边预览边录制，停止预览即停止录制）
	 * 
	 * @author eguid
	 * @param outputFile -录制的文件路径，也可以是rtsp或者rtmp等流媒体服务器发布地址
	 * @param frameRate - 视频帧率
	 * @throws Exception
	 * @throws InterruptedException
	 * @throws org.bytedeco.javacv.FrameRecorder.Exception
	 */
	public static void recordCamera(String outputFile, double frameRate)
			throws Exception, InterruptedException, org.bytedeco.javacv.FrameRecorder.Exception {
		Loader.load(opencv_objdetect.class);
		//抓取参数
		FrameGrabber grabber = FrameGrabber.createDefault(0);//本机摄像头默认0，这里使用javacv的抓取器，至于使用的是ffmpeg还是opencv，请自行查看源码
		grabber.start();//开启抓取器

		OpenCVFrameConverter.ToIplImage converter = new OpenCVFrameConverter.ToIplImage();//转换器
		IplImage grabbedImage = converter.convert(grabber.grab());//抓取一帧视频并将其转换为图像，至于用这个图像用来做什么？加水印，人脸识别等等自行添加
		int width = grabbedImage.width();
		int height = grabbedImage.height();
//录制参数
		FrameRecorder recorder = FrameRecorder.createDefault(outputFile, width, height);
		recorder.setVideoCodec(avcodec.AV_CODEC_ID_H264); // avcodec.AV_CODEC_ID_H264，编码
		recorder.setFormat("flv");//封装格式，如果是推送到rtmp就必须是flv封装格式
		recorder.setFrameRate(frameRate);
		
		recorder.start();//开启录制器
		long startTime=0;
		long videoTS=0;
		CanvasFrame frame = new CanvasFrame("我的直播间", CanvasFrame.getDefaultGamma() / grabber.getGamma());//这里只是抓取视频
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setAlwaysOnTop(true);
		Frame rotatedFrame=converter.convert(grabbedImage);//不知道为什么这里不做转换就不能推到rtmp
		while (frame.isVisible() && (grabbedImage = converter.convert(grabber.grab())) != null) {
			rotatedFrame = converter.convert(grabbedImage);
			frame.showImage(rotatedFrame);
			if (startTime == 0) {
				startTime = System.currentTimeMillis();
			}
			videoTS = 1000 * (System.currentTimeMillis() - startTime);
			recorder.setTimestamp(videoTS);
			recorder.record(rotatedFrame);
			Thread.sleep(40);
		}
		frame.dispose();
		recorder.stop();
		recorder.release();
		grabber.stop();

	}

}
