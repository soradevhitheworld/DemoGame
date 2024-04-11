package gameobject;

import state.GameWorldState;
import effect.Animation;
import effect.CacheDataLoader;

import java.applet.AudioClip;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public class MegaMan extends Human {

    public static final int RUNSPEED = 3;
    
    private Animation runForwardAnim, runBackAnim, runShootingForwarAnim, runShootingBackAnim;
    private Animation idleForwardAnim, idleBackAnim, idleShootingForwardAnim, idleShootingBackAnim;
    private Animation dickForwardAnim, dickBackAnim;
    private Animation flyForwardAnim, flyBackAnim, flyShootingForwardAnim, flyShootingBackAnim;
    private Animation landingForwardAnim, landingBackAnim;
    
    private Animation superPowerForwardAnim,superPowerBackdAnim;
    
    private Animation climWallForward, climWallBack;
    
    private long lastShootingTime;
    private boolean isShooting = false;
    
    private AudioClip hurtingSound;
    private AudioClip shooting1;
    
	private BufferedImage statusBarImg;

	private int statusBarWidth = (int) (192 * 1f);
	private int statusBarHeight = (int) (58 * 1f);
	private int statusBarX = (int) (10 * 1f);
	private int statusBarY = (int) (10 * 1f);
    
    private int powerBarWidth = (int)(12 * 1f);
	private int powerBarHeight = (int) (150 * 1f);
	private int powerBarXStart = (int) (34 * 1f);
	private int powerBarYStart = (int) (150 * 1f);
	private int powerWidth = powerBarWidth;
	private int powerMaxValue = 200;
	private int powerValue = powerMaxValue;
      
    
    public MegaMan(float x, float y, GameWorldState gameWorld) {
        super(x, y, 70, 90, 0.1f, 100, gameWorld);
        powerValue = 0;
        
        shooting1 = CacheDataLoader.getInstance().getSound("bluefireshooting");
        hurtingSound = CacheDataLoader.getInstance().getSound("megamanhurt");
        
        setTeamType(LEAGUE_TEAM);

        setTimeForNoBehurt(2000*1000000);
        
        runForwardAnim = CacheDataLoader.getInstance().getAnimation("run");
        runBackAnim = CacheDataLoader.getInstance().getAnimation("run");
        runBackAnim.flipAllImage();   
        
        idleForwardAnim = CacheDataLoader.getInstance().getAnimation("idle");
        idleBackAnim = CacheDataLoader.getInstance().getAnimation("idle");
        idleBackAnim.flipAllImage();
        
        dickForwardAnim = CacheDataLoader.getInstance().getAnimation("dick");
        dickBackAnim = CacheDataLoader.getInstance().getAnimation("dick");
        dickBackAnim.flipAllImage();
        
        flyForwardAnim = CacheDataLoader.getInstance().getAnimation("flyingup");
        flyForwardAnim.setIsRepeated(false);
        flyBackAnim = CacheDataLoader.getInstance().getAnimation("flyingup");
        flyBackAnim.setIsRepeated(false);
        flyBackAnim.flipAllImage();
        
        landingForwardAnim = CacheDataLoader.getInstance().getAnimation("landing");
        landingBackAnim = CacheDataLoader.getInstance().getAnimation("landing");
        landingBackAnim.flipAllImage();
        
        climWallBack = CacheDataLoader.getInstance().getAnimation("clim_wall");
        climWallForward = CacheDataLoader.getInstance().getAnimation("clim_wall");
        climWallForward.flipAllImage();
        
        behurtForwardAnim = CacheDataLoader.getInstance().getAnimation("hurting");
        behurtBackAnim = CacheDataLoader.getInstance().getAnimation("hurting");
        behurtBackAnim.flipAllImage();
        
        idleShootingForwardAnim = CacheDataLoader.getInstance().getAnimation("idleshoot");
        idleShootingBackAnim = CacheDataLoader.getInstance().getAnimation("idleshoot");
        idleShootingBackAnim.flipAllImage();
        
        runShootingForwarAnim = CacheDataLoader.getInstance().getAnimation("runshoot");
        runShootingBackAnim = CacheDataLoader.getInstance().getAnimation("runshoot");
        runShootingBackAnim.flipAllImage();
        
        flyShootingForwardAnim = CacheDataLoader.getInstance().getAnimation("flyingupshoot");
        flyShootingBackAnim = CacheDataLoader.getInstance().getAnimation("flyingupshoot");
        flyShootingBackAnim.flipAllImage();
        
        superPowerForwardAnim = CacheDataLoader.getInstance().getAnimation("superpower");
        superPowerBackdAnim   =  CacheDataLoader.getInstance().getAnimation("superpower");
        superPowerBackdAnim.flipAllImage();
    }

    @Override
    public void Update() {

        super.Update();        

        if(isShooting){
            if(System.nanoTime() - lastShootingTime > 500*1000000){
                isShooting = false;
            }
        }
        
        if(getIsLanding()){
            landingBackAnim.Update(System.nanoTime());
            if(landingBackAnim.isLastFrame()) {
                setIsLanding(false);
                landingBackAnim.reset();
                runForwardAnim.reset();
                runBackAnim.reset();
            }
        }
    }

    // Phương thức để xử lý sự kiện khi người chơi ấn nút L
    public void handleLButtonPress() {
    	powerSuper();
    }
    
    public void handleLButtonRelease() {
        // Nếu MegaMan đang ở trạng thái SUPERPOWER, chuyển về trạng thái ALIVE
        if (getState() == SUPERPOWER) {
            setState(ALIVE);
        }
    }
 
    
    @Override
    public Rectangle getBoundForCollisionWithEnemy() {
        // TODO Auto-generated method stub
        Rectangle rect = getBoundForCollisionWithMap();
        
        if(getIsDicking()){
            rect.x = (int) getPosX() - 22;
            rect.y = (int) getPosY() - 20;
            rect.width = 44;
            rect.height = 65;
        }else{
            rect.x = (int) getPosX() - 22;
            rect.y = (int) getPosY() - 40;
            rect.width = 44;
            rect.height = 80;
        }
        
        return rect;
    }

    @Override
    public void draw(Graphics2D g2) {
        
        switch(getState()){
        
            case ALIVE:
            case NOBEHURT:
                if(getState() == NOBEHURT && (System.nanoTime()/10000000)%2!=1)
                {
                    System.out.println("Plash...");
                }else{
                    
                    if(getIsLanding()){

                        if(getDirection() == RIGHT_DIR){
                            landingForwardAnim.setCurrentFrame(landingBackAnim.getCurrentFrame());
                            landingForwardAnim.draw((int) (getPosX() - getGameWorld().camera.getPosX()), 
                                    (int) getPosY() - (int) getGameWorld().camera.getPosY() + (getBoundForCollisionWithMap().height/2 - landingForwardAnim.getCurrentImage().getHeight()/2),
                                    g2);
                        }else{
                            landingBackAnim.draw((int) (getPosX() - getGameWorld().camera.getPosX()), 
                                    (int) getPosY() - (int) getGameWorld().camera.getPosY() + (getBoundForCollisionWithMap().height/2 - landingBackAnim.getCurrentImage().getHeight()/2),
                                    g2);
                        }

                    }else if(getIsJumping()){

                        if(getDirection() == RIGHT_DIR){
                            flyForwardAnim.Update(System.nanoTime());
                            if(isShooting){
                                flyShootingForwardAnim.setCurrentFrame(flyForwardAnim.getCurrentFrame());
                                flyShootingForwardAnim.draw((int) (getPosX() - getGameWorld().camera.getPosX()) + 10, (int) getPosY() - (int) getGameWorld().camera.getPosY(), g2);
                            }else
                                flyForwardAnim.draw((int) (getPosX() - getGameWorld().camera.getPosX()), (int) getPosY() - (int) getGameWorld().camera.getPosY(), g2);
                        }else{
                            flyBackAnim.Update(System.nanoTime());
                            if(isShooting){
                                flyShootingBackAnim.setCurrentFrame(flyBackAnim.getCurrentFrame());
                                flyShootingBackAnim.draw((int) (getPosX() - getGameWorld().camera.getPosX()) - 10, (int) getPosY() - (int) getGameWorld().camera.getPosY(), g2);
                            }else
                            flyBackAnim.draw((int) (getPosX() - getGameWorld().camera.getPosX()), (int) getPosY() - (int) getGameWorld().camera.getPosY(), g2);
                        }

                    }else if(getIsDicking()){

                        if(getDirection() == RIGHT_DIR){
                            dickForwardAnim.Update(System.nanoTime());
                            dickForwardAnim.draw((int) (getPosX() - getGameWorld().camera.getPosX()), 
                                    (int) getPosY() - (int) getGameWorld().camera.getPosY() + (getBoundForCollisionWithMap().height/2 - dickForwardAnim.getCurrentImage().getHeight()/2),
                                    g2);
                        }else{
                            dickBackAnim.Update(System.nanoTime());
                            dickBackAnim.draw((int) (getPosX() - getGameWorld().camera.getPosX()), 
                                    (int) getPosY() - (int) getGameWorld().camera.getPosY() + (getBoundForCollisionWithMap().height/2 - dickBackAnim.getCurrentImage().getHeight()/2),
                                    g2);
                        }

                    }else{
                        if(getSpeedX() > 0){
                            runForwardAnim.Update(System.nanoTime());
                            if(isShooting){
                                runShootingForwarAnim.setCurrentFrame(runForwardAnim.getCurrentFrame() - 1);
                                runShootingForwarAnim.draw((int) (getPosX() - getGameWorld().camera.getPosX()), (int) getPosY() - (int) getGameWorld().camera.getPosY(), g2);
                            }else
                                runForwardAnim.draw((int) (getPosX() - getGameWorld().camera.getPosX()), (int) getPosY() - (int) getGameWorld().camera.getPosY(), g2);
                            if(runForwardAnim.getCurrentFrame() == 1) runForwardAnim.setIgnoreFrame(0);
                        }else if(getSpeedX() < 0){
                            runBackAnim.Update(System.nanoTime());
                            if(isShooting){
                                runShootingBackAnim.setCurrentFrame(runBackAnim.getCurrentFrame() - 1);
                                runShootingBackAnim.draw((int) (getPosX() - getGameWorld().camera.getPosX()), (int) getPosY() - (int) getGameWorld().camera.getPosY(), g2);
                            }else
                                runBackAnim.draw((int) (getPosX() - getGameWorld().camera.getPosX()), (int) getPosY() - (int) getGameWorld().camera.getPosY(), g2);
                            if(runBackAnim.getCurrentFrame() == 1) runBackAnim.setIgnoreFrame(0);
                        }else{
                            if(getDirection() == RIGHT_DIR){
                                if(isShooting){
                                    idleShootingForwardAnim.Update(System.nanoTime());
                                    idleShootingForwardAnim.draw((int) (getPosX() - getGameWorld().camera.getPosX()), (int) getPosY() - (int) getGameWorld().camera.getPosY(), g2);
                                }else{
                                    idleForwardAnim.Update(System.nanoTime());
                                    idleForwardAnim.draw((int) (getPosX() - getGameWorld().camera.getPosX()), (int) getPosY() - (int) getGameWorld().camera.getPosY(), g2);
                                }
                            }else{
                                if(isShooting){
                                    idleShootingBackAnim.Update(System.nanoTime());
                                    idleShootingBackAnim.draw((int) (getPosX() - getGameWorld().camera.getPosX()), (int) getPosY() - (int) getGameWorld().camera.getPosY(), g2);
                                }else{
                                    idleBackAnim.Update(System.nanoTime());
                                    idleBackAnim.draw((int) (getPosX() - getGameWorld().camera.getPosX()), (int) getPosY() - (int) getGameWorld().camera.getPosY(), g2);
                                }
                            }
                        }            
                    }
                }
                
                break;
            
            case BEHURT:
                if(getDirection() == RIGHT_DIR){
                    behurtForwardAnim.draw((int) (getPosX() - getGameWorld().camera.getPosX()), (int) getPosY() - (int) getGameWorld().camera.getPosY(), g2);
                }else{
                    behurtBackAnim.setCurrentFrame(behurtForwardAnim.getCurrentFrame());
                    behurtBackAnim.draw((int) (getPosX() - getGameWorld().camera.getPosX()), (int) getPosY() - (int) getGameWorld().camera.getPosY(), g2);
                }
                break;
             
            case FEY:
                
                break;
            case SUPERPOWER:
            	 if (getState() == SUPERPOWER ) {
            		 if(getDirection() == RIGHT_DIR){
                     superPowerForwardAnim.Update(System.nanoTime());
                     superPowerForwardAnim.draw((int) (getPosX() - getGameWorld().camera.getPosX()), 
                                                 (int) (getPosY() - getGameWorld().camera.getPosY()), 
                                                 g2);
                 }else {
                	 superPowerBackdAnim.Update(System.nanoTime());
                	 superPowerBackdAnim.draw((int) (getPosX() - getGameWorld().camera.getPosX()), 
                                                 (int) (getPosY() - getGameWorld().camera.getPosY()), 
                                                 g2);
                 }
              }
            	break;

        }
        
        //drawBoundForCollisionWithMap(g2);
        //drawBoundForCollisionWithEnemy(g2);
        
        // drawbarpower
        int borderThickness = 2; 
        g2.setColor(Color.BLACK);
        g2.drawRoundRect(powerBarXStart - borderThickness, powerBarYStart - borderThickness, 
                powerBarWidth + 2 * borderThickness, powerBarHeight + 2 * borderThickness,
                10, 10); 
        
        int backgroundX = powerBarXStart - borderThickness - 10; 
        int backgroundY = powerBarYStart - borderThickness - 5; 
        int backgroundWidth = powerBarWidth + 2 * borderThickness + 20; 
        int backgroundHeight = powerBarHeight + 2 * borderThickness + 10; 
        
        Color brownRGB = new Color(139, 69, 19); 

        g2.setColor(brownRGB);
        g2.drawRoundRect(backgroundX, backgroundY, backgroundWidth, backgroundHeight,15,15);
        g2.setColor(brownRGB);
        g2.fillRoundRect(backgroundX, backgroundY, backgroundWidth, backgroundHeight, 15, 15);

       
        g2.setColor(Color.WHITE);
        g2.drawRoundRect(powerBarXStart, powerBarYStart, powerBarWidth, powerBarHeight,15,15);
        g2.setColor(Color.GRAY);
        g2.fillRoundRect(powerBarXStart, powerBarYStart, powerBarWidth, powerBarHeight,15,15);
        
        
        int powerBarHeightCurrent = (int) ((float) powerValue / powerMaxValue * powerBarHeight);
       
        g2.setColor(Color.YELLOW);
        g2.drawRoundRect(powerBarXStart, powerBarYStart + powerBarHeight - powerBarHeightCurrent, powerBarWidth, powerBarHeightCurrent, 15, 15);
        g2.setColor(Color.YELLOW);
        g2.fillRoundRect(powerBarXStart, powerBarYStart + powerBarHeight - powerBarHeightCurrent, powerBarWidth, powerBarHeightCurrent, 15, 15);
    }

    @Override
    public void run() {
        if(getDirection() == LEFT_DIR)
            setSpeedX(-3);
        else setSpeedX(3);
    }

    @Override
    public void jump() {

        if(!getIsJumping()){
            setIsJumping(true);
            setSpeedY(-5.0f);           
            flyBackAnim.reset();
            flyForwardAnim.reset();
        }
        // for clim wall
        else{
            Rectangle rectRightWall = getBoundForCollisionWithMap();
            rectRightWall.x += 1;
            Rectangle rectLeftWall = getBoundForCollisionWithMap();
            rectLeftWall.x -= 1;
            
            if(getGameWorld().physicalMap.haveCollisionWithRightWall(rectRightWall)!=null && getSpeedX() > 0){
                setSpeedY(-5.0f);
                //setSpeedX(-1);
                flyBackAnim.reset();
                flyForwardAnim.reset();
                //setDirection(LEFT_DIR);
            }else if(getGameWorld().physicalMap.haveCollisionWithLeftWall(rectLeftWall)!=null && getSpeedX() < 0){
                setSpeedY(-5.0f);
                //setSpeedX(1);
                flyBackAnim.reset();
                flyForwardAnim.reset();
                //setDirection(RIGHT_DIR);
            }
                
        }
    }

    @Override
    public void dick() {
        if(!getIsJumping())
            setIsDicking(true);
    }

    @Override
    public void standUp() {
        setIsDicking(false);
        idleForwardAnim.reset();
        idleBackAnim.reset();
        dickForwardAnim.reset();
        dickBackAnim.reset();
    }

    @Override
    public void stopRun() {
        setSpeedX(0);
        runForwardAnim.reset();
        runBackAnim.reset();
        runForwardAnim.unIgnoreFrame(0);
        runBackAnim.unIgnoreFrame(0);
    }

    @Override
    public void attack() {
    
        if(!isShooting && !getIsDicking() && powerValue == 200){
            
            shooting1.play();
            
            Bullet bullet = new BlueFire(getPosX(), getPosY(), getGameWorld());
            if(getDirection() == LEFT_DIR) {
                bullet.setSpeedX(-10);
                bullet.setPosX(bullet.getPosX() - 40);
                if(getSpeedX() != 0 && getSpeedY() == 0){
                    bullet.setPosX(bullet.getPosX() - 10);
                    bullet.setPosY(bullet.getPosY() - 5);
                }
            }else {
                bullet.setSpeedX(10);
                bullet.setPosX(bullet.getPosX() + 40);
                if(getSpeedX() != 0 && getSpeedY() == 0){
                    bullet.setPosX(bullet.getPosX() + 10);
                    bullet.setPosY(bullet.getPosY() - 5);
                }
            }
            if(getIsJumping())
                bullet.setPosY(bullet.getPosY() - 20);
            
            bullet.setTeamType(getTeamType());
            getGameWorld().bulletManager.addObject(bullet);
            
            lastShootingTime = System.nanoTime();
            isShooting = true;
            
            powerValue = 0;
        }      
    }
    
    @Override
    public void attackBulletNormal() {
  
    if(!isShooting && !getIsDicking() && powerValue <= 200){
        
        shooting1.play();
        
        Bullet BulletYellow = new BulletNormal(getPosX(), getPosY(), getGameWorld());
        if(getDirection() == LEFT_DIR) {
        	BulletYellow.setSpeedX(-20);
        	BulletYellow.setPosX(BulletYellow.getPosX() - 40);
            if(getSpeedX() != 0 && getSpeedY() == 0){
            	BulletYellow.setPosX(BulletYellow.getPosX() - 10);
            	BulletYellow.setPosY(BulletYellow.getPosY() - 5);
            }
        }else {
        	BulletYellow.setSpeedX(20);
        	BulletYellow.setPosX(BulletYellow.getPosX() + 40);
            if(getSpeedX() != 0 && getSpeedY() == 0){
            	BulletYellow.setPosX(BulletYellow.getPosX() + 10);
            	BulletYellow.setPosY(BulletYellow.getPosY() - 5);
            }
        }
        if(getIsJumping())
        	BulletYellow.setPosY(BulletYellow.getPosY() - 20);
        
        BulletYellow.setTeamType(getTeamType());
        getGameWorld().bulletManager.addObject(BulletYellow);
        
        lastShootingTime = System.nanoTime()- 100 * 1000000;
        isShooting = true;
        
    }
   }
    
    
    @Override
    public void hurtingCallback(){
        System.out.println("Call back hurting");
        hurtingSound.play();
    }

	@Override
	public void powerSuper() {
		if (!getIsJumping()) {
		       powerValue++;
		        if(powerValue >200) {
		           powerValue =200;
		        }
		 setState(SUPERPOWER);		
	}
  }
}
