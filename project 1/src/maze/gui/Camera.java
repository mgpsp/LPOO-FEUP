package maze.gui;

class Camera{
	private	double x0, y0, x1, y1;
	private double maxWidth, maxHeight;
	private double minWidth, minHeight;

	/*private boolean setValues(double x0, double y0, double x1, double y1);
	public	Camera(double x0, double y0, double x1, double y1, double limit);
	public	void moveAbs(double x, double y);
	public	void movePartialAbsCentered(double x, double y,  int h_res, int v_res, double extent = 1.0);
	public	void moveRel(double x, double y);
	public	void moveRelScaled(double x, double y, int h_res, int v_res);
	public	void moveRelScreen(double x, double y, int h_res, int v_res);
	public	boolean mulScale(double factorx, double factory);
	public	boolean uncenteredMulScale(double factorx, double factory, double x, double y, int h_res, int v_res);
	public	void addScale(double addx, double addy);
	public	void addScaleUniform(double add, boolean onWidth = true);
	public	double getX()  ;
	public	double getY()  ;
	double getFinalX()  ;
	double getFinalY()  ;
	double getWidth()  ;
	double getHeight()  ;
	double getRenderX(int h_res, double worldX)  ;
	double getRenderY(int v_res, double worldY)  ;
	double getWorldX(int h_res, double renderX)  ;
	double getWorldY(int v_res, double renderY)  ;
	double getZoomScaleX(int h_res) ;
	double getZoomScaleY(int v_res) ;*/


	public	boolean setValues(double x0, double y0, double x1, double y1){
		if(Math.abs(x1-x0) > maxWidth || Math.abs(y1-y0) > maxHeight || Math.abs(x1-x0) < minWidth || Math.abs(y1-y0) < minHeight)
			return false;
		if(x0 < x1){
			this.x0 = x0;
			this.x1 = x1;
		}
		if( y0 < y1){
			this.y0 = y0;
			this.y1 = y1;
		}
		return true;
	}
	public	Camera(double x0, double y0, double x1, double y1, double minWidth,double minHeight, double maxWidth, double maxHeight ){
		this.maxWidth = maxWidth;
		this.maxHeight = maxHeight;
		this.minWidth = minWidth;
		this.minHeight = minHeight;
		setValues(x0, y0, x1, y1);
	}
	public	Camera(double x0, double y0, double x1, double y1, double limit){
		this.maxWidth = (x1 - x0) * limit;
		this.maxHeight = (y1 - y0) * limit;
		this.minWidth = (x1 - x0) / limit;
		this.minHeight = (y1 - y0) / limit;
		setValues(x0, y0, x1, y1);
	}

	public	void moveAbs(double x, double y){
		double deltax = x - x0;
		double deltay = y - y0;
		setValues(x0+deltax,  y0+deltay,x1+deltax, y1+deltay);
	}

	public	void moveRel(double x, double y){
		setValues(x0+x, y0+y,  x1+x, y1+y);
	}

	public	void  moveRelScreen(double x, double y, int h_res, int v_res){
		moveAbs(getWorldX(h_res,  x), getWorldY(v_res, y));
	}
	public	void movePartialAbsCentered(double x, double y,  int h_res, int v_res, double extent){
		moveRel((x-(x1+x0)/2)*extent * Math.min(getZoomScaleX(h_res), 1),(y-(y1+y0)/2)*extent * Math.min(getZoomScaleY(v_res), 1));
	}

	public	boolean mulScale(double factorx, double factory){
		if(factorx == 0 || factory == 0)
			return false;
		if(factorx < 0)
			factorx = -factorx;
		if(factory < 0)
			factory = -factory;
		double xmul = (x1-x0)* factorx;
		double ymul = (y1-y0)* factory;
		return setValues(((x1+x0) - xmul)/2, ((y1+y0) - ymul)/2, ((x1+x0) +xmul)/2,((y1+y0) + ymul)/2);
	}
	public	boolean uncheckedMulScale(double factorx, double factory){
		if(factorx == 0 || factory == 0)
			return false;
		if(factorx < 0)
			factorx = -factorx;
		if(factory < 0)
			factory = -factory;
		double xmul = (x1-x0)* factorx;
		double ymul = (y1-y0)* factory;
		x0 = ((x1+x0) - xmul)/2;
		y0=	 ((y1+y0) - ymul)/2;
		x1 = ((x1+x0) +xmul)/2;
		y1 = ((y1+y0) + ymul)/2;
		return true;
	}

	public	boolean uncenteredMulScale(double factorx, double factory, double x, double y, int h_res, int v_res){
		x = getWorldX(h_res, x);
		y = getWorldY(v_res, y);
		double posx = factorx*x0+(1-factorx)*x;
		double posy = factory*y0+(1-factory)*y;
		if(mulScale(factorx, factory))
		{moveAbs(posx, posy); return true;}
		return false;
	}

	public void moveAbsCenteredScreen(int x, int y, int h_res, int v_res){
		double widthx = x1 - x0;
		double widthy = y1 - y0;
		double newCenterx = getWorldX(h_res, x);
		double newCentery = getWorldY(v_res, y);
		x0 = newCenterx - widthx/2;
		y0 = newCentery - widthy/2;
		x1 = newCenterx + widthx/2;
		y1 = newCentery + widthy/2;
	}

	public	void addScale(double addx, double addy){
		setValues(x0 - addx/2, y0 - addy/2, x1 + addx/2, y1 + addx/2);
	}

	public void addScaleUniform(double add, boolean onWidth){
		if(onWidth)
			addScale(add, add * (y1-y0)/(x1-x0));
		else
			addScale(add* (x1-x0)/(y1-y0), add);
	}

	public	double  getX()   {return x0;}
	public	double  getY()   {return y0;}
	public	double  getFinalX()   {return x1;}
	public	double  getFinalY()   {return y1;}
	public	double  getWidth()   {return x1-x0;}
	public	double  getHeight()   {return y1- y0;}
	public	double  getRenderX(int h_res, double worldX)  {
		return (worldX-x0)*h_res/(x1-x0);
	}
	public	double  getRenderY(int v_res, double worldY)  {
		return (worldY-y0)*v_res/(y1-y0);
	}

	public	double  getWorldX(int h_res, double renderX)  {
		return x0+renderX*(x1-x0)/h_res;
	}
	public	double  getWorldY(int v_res, double renderY)  {
		return y0+renderY*(y1-y0)/v_res;
	}

	public	double  getZoomScaleX(int h_res) {
		return ((double)(x1-x0))/h_res;
	}
	public	double  getZoomScaleY(int v_res) {
		return ((double)(y1-y0))/v_res;
	}
	public	void  moveRelScaled(double x, double y, int h_res, int v_res){
		double movex = x*getZoomScaleX(h_res);
		double movey = y*getZoomScaleY(v_res);
		if(movex == 0 && x != 0){
			if(x < 0)
				movex =-1;
			else movex = 1;
		}
		if(movey == 0 && y != 0){
			if(y < 0)
				movey =-1;
			else movey = 1;
		}
		moveRel(movex, movey);
	}
	public double getAspectRatio(){
		return (x1-x0)/(y1-y0);
	}
	public boolean atLowerBound(){
		return (Math.abs(x1-x0 - minWidth) < 0.001) && (Math.abs(y1-y0 - minHeight) < 0.001);
	}
	public boolean moveAbsCentered(double x, double y){
		return setValues(x-(x1-x0)/2, y-(y1-y0)/2,x+(x1-x0)/2,y+(y1-y0)/2);
	}
}
