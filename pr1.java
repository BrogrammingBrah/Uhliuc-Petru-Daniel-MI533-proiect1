
import java.applet.Applet;
import java.awt.Button;
import java.awt.Image;
import java.awt.Point;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Event;

public class pr1 extends Applet{
	Point[] cp;
	Point[] pctmed; //pct mediane
	Point[] pctbis; //pct bisect
	Point[] pctinl; //pct inaltimi
	Button mediana;
	Button bisectoare;
	Button inaltime;
	Button restart;
	Graphics imgr;
	Image img;
	int nrpcte;
	int moveflag;
	boolean desenezmed;
	boolean desenezbis;
	boolean desenezinl;
	String[] lit={ "A", "B", "C", "G", "H", "I" };
	double rCC; //r cercului circumscris 
	Point cCC; //centrul -||-
	int r; //raza
	boolean trgv; //deplasez cu mouse-ul de pe un pct al triunghiului
	boolean trgc; ///deplasez cu mouse-ul de pe un punct al cercului
	boolean mouseUp;
	
	public void init(){
		cp=new Point[3];
		pctmed=new Point[3];
		pctbis=new Point[3];
		pctinl=new Point[3];
		img=createImage(3000,3000);
		imgr=img.getGraphics();
		restart=new Button("restart");
		mediana=new Button("mediana");
		bisectoare=new Button("bisectoare");
		inaltime=new Button("inaltime");
		add(restart);
		add(mediana);
		add(bisectoare);
		add(inaltime);
		desenezmed=false;
		desenezbis=false;
		desenezinl=false;
		mouseUp=false;
		//butoanele 
	}
	
	public void update(Graphics g) {
		paint(g);
	}
	
	public void paint(Graphics g) {
		setBackground(Color.white);
		imgr.setColor(Color.black);
		imgr.clearRect(0, 0, size().width, size().height);
			if (nrpcte < 3) {
			mediana.setEnabled(false);
			bisectoare.setEnabled(false);
			inaltime.setEnabled(false);
		}
		if (nrpcte == 3) {
			mediana.setEnabled(true);
			bisectoare.setEnabled(true);
			inaltime.setEnabled(true);
			double AB = dist(cp[0], cp[1]);
			double BC = dist(cp[1], cp[2]);
			double CA = dist(cp[2], cp[0]); 
			double A = sin(BC, AB, CA); //unghiul A
			double B = sin(CA, AB, BC); //unghiul B
			double C = sin(AB, BC, CA); // unghiul C, AB,BC,CA sunt laturi 
			cCC = cC(A, B, C);
		
			if (trgc) {
				cp[0] = coordpctn(cp[0], cCC); 
				cp[1] = coordpctn(cp[1], cCC);
				cp[2] = coordpctn(cp[2], cCC); 
				r = (int) Math.round(rCC); 
			} else {
				r = (int) Math.round(getr(AB, BC, CA));
				rCC = r; //raza cerc circumscris
			}
		
			imgr.setColor(Color.blue);
			imgr.fillOval(cCC.x - 3, cCC.y - 3, 6, 6);
			imgr.setColor(Color.blue);
			imgr.drawOval(cCC.x - r, cCC.y - r, 2 * r, 2 * r);
			
			
		}
		if (nrpcte == 3 && desenezmed) {
			pctmed[0] = mediana(cp[1], cp[2]);
			pctmed[1] = mediana(cp[2], cp[0]);
			pctmed[2] = mediana(cp[0], cp[1]);
			imgr.setColor(Color.yellow.darker());
			imgr.fillOval(pctmed[0].x - 3, pctmed[0].y - 3, 3, 3);
			imgr.fillOval(pctmed[1].x - 3, pctmed[1].y - 3, 3, 3);
			imgr.fillOval(pctmed[2].x - 3, pctmed[2].y - 3, 3, 3);
			imgr.setColor(Color.DARK_GRAY);
			imgr.drawLine(cp[0].x, cp[0].y, pctmed[0].x, pctmed[0].y);
			imgr.drawLine(cp[1].x, cp[1].y, pctmed[1].x, pctmed[1].y);
			imgr.drawLine(cp[2].x, cp[2].y, pctmed[2].x, pctmed[2].y);
			
			Point intersectmed = intersmed(cp[0], cp[1], cp[2]);
			//imgr.setColor(Color.green);
			imgr.setColor(Color.red);
			imgr.fillOval(intersectmed.x - 3, intersectmed.y - 3, 3, 3);
			imgr.setColor(Color.black);
			imgr.drawString(lit[3], intersectmed.x + 3, intersectmed.y + 3);
		}
		if (nrpcte == 3 && desenezbis) {
			pctbis[0] = pctbis(cp[1], cp[2], cp[0]);
			pctbis[1] = pctbis(cp[2], cp[0], cp[1]);
			pctbis[2] = pctbis(cp[1], cp[0], cp[2]);
			imgr.setColor(Color.yellow.darker());
			imgr.fillOval(pctbis[0].x - 3, pctbis[0].y - 3, 3, 3);
			imgr.fillOval(pctbis[1].x - 3, pctbis[1].y - 3, 3, 3);
			imgr.fillOval(pctbis[2].x - 3, pctbis[2].y - 3, 3, 3);
			imgr.setColor(Color.DARK_GRAY);
			imgr.drawLine(cp[0].x, cp[0].y, pctbis[0].x, pctbis[0].y);
			imgr.drawLine(cp[1].x, cp[1].y, pctbis[1].x, pctbis[1].y);
			imgr.drawLine(cp[2].x, cp[2].y, pctbis[2].x, pctbis[2].y);
		    Point intersectbis = intersbis(cp[0], cp[1], cp[2]);
	//		imgr.setColor(Color.green);
			imgr.setColor(Color.red);
			imgr.fillOval(intersectbis.x - 3, intersectbis.y - 3, 3, 3);
			imgr.setColor(Color.black);
			imgr.drawString(lit[5], intersectbis.x + 3, intersectbis.y + 3);
		}
		if (nrpcte == 3 && desenezinl) {
			pctinl[0] = pctinl(cp[0], cp[1], cp[2]);
			pctinl[1] = pctinl(cp[1], cp[2], cp[0]);
			pctinl[2] = pctinl(cp[2], cp[0], cp[1]);
			imgr.setColor(Color.yellow.darker());
			imgr.fillOval(pctinl[0].x - 3, pctinl[0].y - 3, 3, 3);
			imgr.fillOval(pctinl[1].x - 3, pctinl[1].y - 3, 3, 3);
			imgr.fillOval(pctinl[2].x - 3, pctinl[2].y - 3, 3, 3);
			imgr.setColor(Color.DARK_GRAY);
			imgr.drawLine(cp[0].x, cp[0].y, pctinl[0].x, pctinl[0].y);
			imgr.drawLine(cp[1].x, cp[1].y, pctinl[1].x, pctinl[1].y);
			imgr.drawLine(cp[2].x, cp[2].y, pctinl[2].x, pctinl[2].y);
			Point intersectinl = intersinl(cp[0], cp[1], cp[2]);
			imgr.setColor(Color.red);
			imgr.fillOval(intersectinl.x - 3, intersectinl.y - 3, 3, 3);
			imgr.setColor(Color.black);
			imgr.drawString(lit[4], intersectinl.x + 3, intersectinl.y + 3);
		}
		if (cp.length > 0) {
			if (nrpcte > 1) {
				for (int i = 0; i < nrpcte; i++) {
					imgr.setColor(Color.black);
					if (i + 1 == nrpcte) {
						imgr.drawLine(cp[i].x, cp[i].y, cp[0].x, cp[0].y);
					} else {
						imgr.drawLine(cp[i].x, cp[i].y, cp[i + 1].x, cp[i + 1].y);
					}
				}
			}
			for (int i = 0; i < nrpcte; i++) {
				imgr.setColor(Color.yellow);
				imgr.fillOval(cp[i].x - 3, cp[i].y - 3, 6, 6);
				imgr.setColor(Color.black);
				imgr.drawString(lit[i], cp[i].x + 3, cp[i].y + 3);
			}

		}

		g.drawImage(img, 0, 0, this);
	}
	
	public boolean action(Event e, Object o) {
		if (((Button) e.target).getLabel() == "restart") {
			cp = new Point[3];
			nrpcte = 0;
			desenezmed= false;
			desenezbis= false;
			desenezinl = false;
			repaint();
			return true;
		}
		if (((Button) e.target).getLabel() == "mediana" && nrpcte == 3) {
			if (desenezmed) {
				desenezmed = false;
				repaint();
				return true;
			} else {
				desenezmed = true;
				pctmed = new Point[3];
				desenezbis = false;
				desenezinl = false;
				repaint();
				return true;
			}
		}
		if (((Button) e.target).getLabel() == "bisectoare" && nrpcte == 3) {
			if (desenezbis) {
				desenezbis = false;

				repaint();
				return true;
			} else {
				desenezbis = true;
				pctbis = new Point[3];
				desenezmed = false;
				desenezinl = false;
				repaint();
				return true;
			}
		}

		if (((Button) e.target).getLabel() == "inaltime" && nrpcte == 3) {
			if (desenezinl) {
				desenezinl =false;

				repaint();
				return true;
			} else {
				desenezinl = true;
				pctinl = new Point[3];
				desenezmed = false;
				desenezbis = false;
				repaint();
				return true;
			}
		}

		return false;
	}
	
		
	public boolean mouseDown(Event evt, int x, int y) {
		Point pct = new Point(x, y);

		if (nrpcte != 3) {
			cp[nrpcte] = pct;
			nrpcte++;
		} else {
			for (int i = 0; i < cp.length; i++) {
				for (int j = -8; j < 9; j++)
					for (int k = -8; k < 9; k++)
						if (pct.equals(new Point(cp[i].x + j, cp[i].y + k))) {
							moveflag = i;
							trgv = true;
						}
			}
			if (!trgv) {
				for (int j = -8; j < 9; j++) {
					for (int k = -8; k < 9; k++) {

						if (Math.round(rCC) == Math.round(dist(cCC, new Point(x + j, y + k)))) {
							trgc = true;
						}

					}
				}
			}
		}
		repaint();
		return true;
	}
	
	public boolean mouseDrag(Event eve, int x, int y) {

		if (trgc) {
			rCC = (int) Math.round(dist(cCC, new Point(x, y)));

		}
		if (trgv) {
			cp[moveflag].move(x, y);
		}
		repaint();
		return true;
	}

	public boolean mouseUp(Event eve, int x, int y) {

		trgv = false;
		if (trgc) {
			trgc = false;
			trgv=false;
			mouseUp = true;
		}
		else
			if(trgv){
				trgc = false;
				trgv=false;
				mouseUp = true;
			}

		return true;
	}
	
	public double dist(Point p1, Point p2) {
		double d = 0;
		d = Math.sqrt(Math.pow(p1.x - p2.x, 2) + Math.pow(p1.y - p2.y, 2));
		return d;
	}
	
	public Point cC(double A, double B, double C) {
		Point p = new Point();

		
		double x = (cp[0].x * A + cp[1].x * B + cp[2].x * C) / (A + B + C);
		double y = (cp[0].y * A + cp[1].y * B + cp[2].y * C) / (A + B + C);
		p.setLocation(x, y);
		return p;
	}
	
	public double sin(double latOU, double latst, double latdr) {
		double result = (Math.pow(latst, 2) + Math.pow(latdr, 2) - Math.pow(latOU, 2))	/ (2 * latst * latdr);
		result = Math.sin(2 * Math.acos(result));
		return result;
	}
	
	public double getr(double latOU, double latst, double latdr) {
		double result = (latOU * latst * latdr)	/ (Math.sqrt((latOU + latdr + latst) * (latOU + latdr - latst)	* (latOU - latdr + latst) * (latdr + latst - latOU)));
		return result;
	}

	public Point mediana(Point vfst, Point vfdr) {
		Point p = new Point();
		p.x = (vfst.x + vfdr.x) / 2;
		p.y = (vfst.y + vfdr.y) / 2;
		return p;
	}

	public double getpanta(Point vfst, Point vfdr) {
		double m = (double) (vfst.y - vfdr.y) / (double) (vfst.x - vfdr.x);
		return m;
	}

	public double getdistpbis(double latst, double latdr, double latOU) {  //dist pana la piciorul bisectoarei
		double x = 0;
		x = (latOU * latst) / (latdr + latst);
		return x;
	}
	
	public Point pctbis(Point vfst, Point vfdr, Point vf) {
		Point pctbis = new Point();
		double panta = getpanta(vfst, vfdr);
		double latOU = dist(vfst, vfdr);
		double latst = 0;
		double latdr = 0;
		Point pctplec = new Point();
		if (vfst.x <= vfdr.x) {
			latst = dist(vf, vfst);
			latdr = dist(vf, vfdr);
			pctplec = vfst;
		} else {
			latst = dist(vf, vfdr);
			latdr = dist(vf, vfst);
			pctplec = vfdr;
		}
		double dist = getdistpbis(latst, latdr, latOU);
		pctbis.x = (int) Math.round(pctplec.x + dist * Math.sqrt(1 / (1 + Math.pow(panta, 2))));
		pctbis.y = (int) Math.round(pctplec.y + panta * dist * Math.sqrt(1 / (1 + Math.pow(panta, 2))));
		return pctbis;
	}

	public Point pctinl(Point vf, Point vfst, Point vfdr) {
		Point pctinl = new Point();
		if (vfst.x <= vfdr.x) {

		} else {
			Point pointHolder = new Point();
			pointHolder = vfst;
			vfst = vfdr;
			vfdr = pointHolder;
		}
		double pantainl = (-1.0) / getpanta(vfst, vfdr);
		double pantabaza = getpanta(vfst, vfdr);
		pctinl.x = (int) Math.round((vfst.y - vf.y + pantainl * vf.x - vfst.x * pantabaza)/ (pantainl - pantabaza));
		pctinl.y = (int) Math.round(vf.y - pantainl * (vf.x - pctinl.x));

		return pctinl;
	}

	public Point intersmed(Point vf, Point vfst, Point vfdr) {
		Point p = new Point();
		p.x = (vf.x + vfst.x + vfdr.x) / 3;
		p.y = (vf.y + vfst.y + vfdr.y) / 3;
		return p;
	}

	public Point intersbis(Point vf, Point vfst, Point vfdr) {
		Point p = new Point();
		p.x = (int) Math.round(((dist(vfst, vfdr) * vf.x + dist(vf, vfdr) * vfst.x	+ dist(vf, vfst) * vfdr.x)	/ (dist(vfst, vfdr) + dist(vf, vfdr) + dist(vf, vfst))));
		p.y = (int) Math.round(((dist(vfst, vfdr) * vf.y + dist(vf, vfdr) * vfst.y	+ dist(vf, vfst) * vfdr.y)	/ (dist(vfst, vfdr) + dist(vf, vfdr) + dist(vf, vfst))));
		return p;
	}

	public Point intersinl(Point vf, Point vfst, Point vfdr) {
		Point p = new Point();
		double pantaAB = getpanta(vf, vfst);
		double pantaCF = (-1.0) / pantaAB;
		double pantaBC = getpanta(vfst, vfdr);
		double pantaAD = (-1.0) / pantaBC;
	 	p.y = (int) Math.round(((pantaCF * vf.y + pantaCF * pantaAD * vfdr.x - pantaCF * pantaAD * vf.x	- pantaAD * vfdr.y) / (pantaCF - pantaAD)));
		p.x = (int) Math.round(((pantaCF * vfdr.x - vfdr.y + p.y) / (pantaCF)));
		return p;
	}
	
	public Point coordpctn(Point pct, Point cC) {
		Point p = new Point();
		double panta = getpanta(pct, cC);
		if (pct.y >= cC.y) {
			p.y = (int) Math.round(rCC / (Math.sqrt(1 / (panta * panta) + 1)) + cC.y);
			p.x = (int) Math.round((p.y - cC.y) / panta + cC.x);
		} else {
			panta = getpanta(cC, pct);
			p.y = (int) Math.round(rCC / (Math.sqrt(1 / (panta * panta) + 1)) + cC.y);
			p.x = (int) Math.round((p.y - cC.y) / -panta + cC.x);

		}

	
		return p;
	}

}
