	package gameobject;
	
	import state.GameWorldState;
	import effect.Animation;
	import effect.CacheDataLoader;
	import java.awt.Graphics2D;
	import java.awt.Rectangle;
	
	public  class BulletNormal extends Bullet{
		
		  private Animation forwardBulletAnim, backBulletAnim;
		    
		    public BulletNormal(float x, float y, GameWorldState gameWorld) {
		            super(x, y, 30, 30, 1.0f, 5, gameWorld);
		            forwardBulletAnim = CacheDataLoader.getInstance().getAnimation("bluefire");
		            backBulletAnim = CacheDataLoader.getInstance().getAnimation("bluefire");
		            backBulletAnim.flipAllImage();
		    }
	
		    
		    
		    @Override
		    public Rectangle getBoundForCollisionWithEnemy() {
		            // TODO Auto-generated method stub
		            return getBoundForCollisionWithMap();
		    }
	
		    @Override
		    public void draw(Graphics2D g2) {
		            // TODO Auto-generated method stub
		        if(getSpeedX() > 0){          
		            forwardBulletAnim.Update(System.nanoTime());
		            forwardBulletAnim.draw((int) (getPosX() - getGameWorld().camera.getPosX()), (int) getPosY() - (int) getGameWorld().camera.getPosY(), g2);
		        }else{
		            backBulletAnim.Update(System.nanoTime());
		            backBulletAnim.draw((int) (getPosX() - getGameWorld().camera.getPosX()), (int) getPosY() - (int) getGameWorld().camera.getPosY(), g2);
		        }
		        //drawBoundForCollisionWithEnemy(g2);
		    }
	
		    @Override
		    public void Update() {
		            // TODO Auto-generated method stub
		        super.Update();
		    }
	
		    @Override
		    public void attack() {}
	
	
	
			@Override
			public void attackBulletNormal() {
				// TODO Auto-generated method stub
				
			}
	
	
	
	
	}
