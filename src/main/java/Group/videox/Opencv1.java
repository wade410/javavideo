package Group.videox;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.bytedeco.javacv.CanvasFrame;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.FrameGrabber.Exception;
import org.bytedeco.javacv.OpenCVFrameConverter;
import org.bytedeco.javacv.OpenCVFrameGrabber;

import org.bytedeco.opencv.global.opencv_imgcodecs;
import org.bytedeco.opencv.global.opencv_imgproc;
import org.bytedeco.opencv.opencv_core.Mat;
import org.bytedeco.opencv.opencv_core.Point;
import org.bytedeco.opencv.opencv_core.Scalar;

public class Opencv1 {
	public static void main(String[] args) throws Exception, InterruptedException{
//		while(true){
			new Opencv1().test();	
//		}
		
	}
	public void test() throws Exception, InterruptedException{
		// 转换器，用于Frame/Mat/IplImage相互转换
					OpenCVFrameConverter.ToIplImage converter = new OpenCVFrameConverter.ToIplImage();
			 
					// 使用OpenCV抓取本机摄像头，摄像头设备号默认0
					OpenCVFrameGrabber grabber = new OpenCVFrameGrabber(0);
			 
					// 开启抓取器
					grabber.start();
			 
					//做好自己！--eguid版权所有，转载请注明出处！
					CanvasFrame cFrame = new CanvasFrame("视频抓取", CanvasFrame.getDefaultGamma() / grabber.getGamma());
					cFrame.setAlwaysOnTop(true);
					cFrame.setSize(500, 600);
					cFrame.setVisible(true);
					// 水印文字位置
					Point point1 = new Point(10, 50);
					Point point2 = new Point(200, 200);
					Point point3 = new Point(200, 240);
					// 颜色
					Scalar scalar1 = new Scalar(0, 255, 255, 0);
					Scalar scalar2 = new Scalar(255, 0, 0, 0);
					Frame frame = null;
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					int index = 0;
					// 关闭窗口即停止运行
					
					while (cFrame.isShowing()) {
//						if ((frame = grabber.grabFrame()) != null) {//这个取图片的操作
						if ((frame = grabber.grab()) != null) {
							// 取一帧视频（图像），并转换为Mat
							Mat mat = converter.convertToMat(grabber.grabFrame());
			 
							// 加文字水印，opencv_imgproc.putText（图片，水印文字，文字位置，字体，字体大小，字体颜色，字体粗度，文字反锯齿，是否翻转文字）
							opencv_imgproc.putText(mat, "facs!", point2, opencv_imgproc.CV_FONT_VECTOR0, 2.2, scalar2, 1, 0,
									false);
							// 翻转字体，文字平滑处理（即反锯齿）
							opencv_imgproc.putText(mat, "facs!", point3, opencv_imgproc.CV_FONT_VECTOR0, 2.2, scalar2, 1, 20,
									true);
			 
							opencv_imgproc.putText(mat, sdf.format(new Date()), point1, opencv_imgproc.CV_FONT_ITALIC, 0.8, scalar1,
									2, 20, false);
							 //在窗口显示处理后的图像，
							cFrame.showImage(converter.convert(mat));
//							cFrame.showImage(grabber.grab());
							if (index == 0) {
								// 保存第一帧图片到本地
								opencv_imgcodecs.imwrite("D:/eguid.jpg", mat);
							}
							// 释放Mat资源
//							mat.release();
//							mat.close();
			 
						}
					
						Thread.sleep(40);
						index++;
					}
			 
					cFrame.dispose();// 销毁窗口
					grabber.stop();// 停止抓取器

			 
					// 手动释放资源
					scalar1.close();
					scalar2.close();
					point1.close();
					point2.close();
					point3.close();
	}
	


}
