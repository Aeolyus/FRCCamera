
package org.usfirst.frc.team2854.robot;

import com.ni.vision.NIVision;
import com.ni.vision.NIVision.DrawMode;
import com.ni.vision.NIVision.Image;
import com.ni.vision.NIVision.ShapeMode;

import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.vision.USBCamera;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {
	USBCamera usbcamera;
	CameraServer server1;
	String camera;
	Joystick joy;
	private Image frame;
	
	private int cam0 = 0;
	private int cam1 = 1;
	private int currCam;
	 NIVision.Rect rect;
	
    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() {
    	joy = new Joystick(0);
    	
    	cam0 = NIVision.IMAQdxOpenCamera("cam0", NIVision.IMAQdxCameraControlMode.CameraControlModeController);
        cam1 = NIVision.IMAQdxOpenCamera("cam1", NIVision.IMAQdxCameraControlMode.CameraControlModeController);
        currCam = cam0;

        frame = NIVision.imaqCreateImage(NIVision.ImageType.IMAGE_RGB, 0);

        rect = new NIVision.Rect(10, 10, 100, 100);
        changeCam(cam0);
////        
//        server1 = CameraServer.getInstance();
//        server1.setQuality(50);
//        server1.startAutomaticCapture();
//        //the camera name (ex "cam0") can be found through the roborio web interface
//        
//        frame = NIVision.imaqCreateImage(NIVision.ImageType.IMAGE_RGB, 0);
        
//        
//
//    	usbcamera = new USBCamera("cam0");
//    	 usbcamera.openCamera();
//         usbcamera.startCapture();
         
    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {

    }

    /**
     * This function is called periodically during operator control
     */
    boolean pressed = false;
    public void teleopPeriodic() {
    	updateCam();
    	if (joy.getRawButton(2)==true && !pressed){
    		System.out.println("Button pressed");
    		pressed = true;
    		if (currCam == cam0){
    			System.out.println("Switched to cam1");
    			changeCam(cam1);
    		} else{
    			System.out.println("Switched to cam0");
    			changeCam(cam0);
    		}
    	}else if(!joy.getRawButton(1)){
    		pressed = false;
    	}
    	Timer.delay(0.005);	
       
    }
    
    public void updateCam()
    {
    	NIVision.IMAQdxGrab(currCam, frame, 1);
        NIVision.imaqDrawShapeOnImage(frame, frame, rect,
                DrawMode.DRAW_VALUE, ShapeMode.SHAPE_OVAL, 0.0f);
        
        CameraServer.getInstance().setImage(frame);
    }
    
	public void changeCam(int newId)
    {
		NIVision.IMAQdxStopAcquisition(currCam);
    	NIVision.IMAQdxConfigureGrab(newId);
    	NIVision.IMAQdxStartAcquisition(newId);
    	currCam = newId;
		
    }
    
    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() {
    
    }
    
}
