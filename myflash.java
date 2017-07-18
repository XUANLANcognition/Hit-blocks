import javax.swing.*;
import javax.tools.Tool;
import javax.xml.bind.annotation.XmlType;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.time.Year;

/**
 * Created by guoli on 2017/6/8.
 */

/*
This class include some static variable :
    the location of the ball
    the speed of the ball
    the condition of the ball
    the weight and height of the screen and panel
 */

class var {
    static int x = 0;
    static int y = 0;
    static int speedx = 0;
    static int speedy = 0;
    static int currentCondition = 0;
    static int ballWidth = 40;
    static int ballHeight = 40;
    static int panelHeight;
    static int panelWidth;
    static int screenWidth;
    static int screenHeight;
    static int boardx = 0;
}

/*
myflash is the main body
 */

class myflash extends JFrame implements Runnable {
    Thread thread;
    static blocks bb;

    public myflash() {
        Toolkit kit = Toolkit.getDefaultToolkit();
        Image image = kit.createImage("1.png");
        this.setIconImage(image);
        Dimension screensize = kit.getScreenSize();
        var.screenWidth = screensize.width / 2;
        var.screenHeight = screensize.height / 2;
        init();
        setSize(var.screenWidth, var.screenHeight);
        setLocationRelativeTo(null);
        setVisible(true);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
    }

    public void init() {
        Container container = this.getContentPane();
        JMenuBar jMenuBar = new JMenuBar();
        JMenuItem jMenuItem = new JMenuItem("��ͣ");
        jMenuItem.setFont(new Font("����", Font.BOLD, 28));
        jMenuItem.addActionListener(event -> myKeyListener.pause());
        JMenuItem jMenuItem1 = new JMenuItem("��ʼ");
        jMenuItem1.addActionListener(event -> speedCon(2, 6));
        jMenuItem1.setFont(new Font("����", Font.BOLD, 28));
        JMenuItem jMenuItem2 = new JMenuItem("����");
        jMenuItem2.setFont(new Font("����", Font.BOLD, 28));
        jMenuItem2.addActionListener(event -> myKeyListener.contiue());
        JMenuItem jMenuItem3 = new JMenuItem("��ݼ�");
        jMenuItem3.addActionListener(event -> new keyBoard());
        jMenuItem3.setFont(new Font("����", Font.BOLD, 28));
        JMenuItem jMenuItem4 = new JMenuItem("��Ϸ˵��");
        jMenuItem4.addActionListener(event -> new explain());
        jMenuItem4.setFont(new Font("����", Font.BOLD, 28));
        JMenuItem jMenuItem5 = new JMenuItem("����");
        jMenuItem5.addActionListener(event -> new about());
        jMenuItem5.setFont(new Font("����", Font.BOLD, 28));
        JMenu jMenu = new JMenu("�˵�");
        JMenu jMenu1 = new JMenu("����");
        JMenu jMenu2 = new JMenu("����");
        jMenu.setFont(new Font("����", Font.BOLD, 28));
        jMenu1.setFont(new Font("����", Font.BOLD, 28));
        jMenu2.setFont(new Font("����", Font.BOLD, 28));
        jMenu.add(jMenuItem1);
        jMenu.add(jMenuItem);
        jMenu.add(jMenuItem2);
        jMenu1.add(jMenuItem3);
        jMenu1.add(jMenuItem4);
        jMenu2.add(jMenuItem5);
        jMenuBar.add(jMenu);
        jMenuBar.add(jMenu1);
        jMenuBar.add(jMenu2);
        add(new myPaint());     //����Ϸ��������
        container.add(jMenuBar, BorderLayout.NORTH);
        thread = new Thread(this);
        thread.start();
        addKeyListener(new myKeyListener());
    }

    @Override
    public void run() {
        int count = 0;
        int m = 0;
        conmentsPass pass = new conmentsPass();
        double xx, yy;
        double xxb, yyb;
        while (true) {
            bb = pass.getMyblocks()[m];
            if (count == bb.getCountBlocks()) {
                m = m + 1;
                if (m == 10) {
                    JOptionPane.showMessageDialog(null, "��ϲ��ͨ�سɹ����������",
                            "��Ϸ", JOptionPane.INFORMATION_MESSAGE, null);
                    System.exit(0);
                }
                myPaint.countS = myPaint.countS + 1;
                count = 0;
                var.x = 0;
                var.y = 0;
                JOptionPane.showMessageDialog(null, "��ϲ������" + (m + 1) + "��",
                        "��Ϸ", JOptionPane.INFORMATION_MESSAGE, null);
                var.speedx = 0;
                var.speedy = 0;
            }
            for (int i = 0; i < bb.getB().length; i++)
                for (int j = 0; j < bb.getB()[i].length; j++) {
                    if (bb.getB()[i][j].getCurrentCondition()) {
                        xx = (double) var.x + 20.0;
                        yy = (double) var.y + 20.0;
                        xxb = (double) bb.getB()[i][j].getBlockLocationX() + 20.0;
                        yyb = (double) bb.getB()[i][j].getBlockLocationY() + 20.0;
                        if ((int) Math.pow((Math.pow(xx - xxb, 2) + Math.pow(yy - yyb, 2)), 0.5) < 40) {
                            var.speedx = -var.speedx;
                            var.speedy = -var.speedy;
                            bb.getB()[i][j].setCurrentCondition(false);
                            count++;
                        }
                    }
                }
            currentCon();
            switch (var.currentCondition) {
                case 1:
                    var.speedy = -var.speedy;
                    break;
                case 2:
                    var.speedx = -var.speedx;
                    break;
                case 3:
                    var.speedy = -var.speedy;
                    break;
                case 4:
                    var.speedx = -var.speedx;
                    break;

            }
            var.x = var.x + var.speedx;
            var.y = var.y + var.speedy;
            var.currentCondition = 0;       //����ǰ��״̬����
            try {
                thread.sleep(16);
            } catch (InterruptedException e) {
                System.out.println("error");
            }
            repaint();

        }
    }

    public void currentCon() {
        if (var.y < 0) {
            var.currentCondition = 1;
            return;
            //С�����ϳ���
        } else if (var.x < 5) {
            var.currentCondition = 2;
            var.x = var.x + 5;
            return;
            //С���������
        } else if ((var.y + var.ballHeight > var.panelHeight) && (var.x >= var.boardx - var.ballWidth / 2)
                && (var.x <= var.boardx + 100 - var.ballWidth / 2)) {
            var.currentCondition = 3;
            return;
            //��С����ײ��������
        } else if (var.x + var.ballWidth > var.panelWidth) {
            var.currentCondition = 4;
            return;
            //С�����ҳ���
        } else if (var.y + var.ballHeight > var.panelHeight) {
            JOptionPane.showMessageDialog(null, "��Ϸ����",
                    "��Ϸ", JOptionPane.INFORMATION_MESSAGE, null);
            System.exit(0);         //С������û��ײ�����������
        } else {
            var.currentCondition = 0;
        }
    }

    /*
    ����С���ٶ�
     */
    public static void speedCon(int x, int y) {
        var.speedx = x;
        var.speedy = y;
    }


    public static void main(String[] args) {
        myflash m = new myflash();
    }
}

/*
paint on panel, and add to frame
 */

class myPaint extends JPanel {
    static int countS = 1;

    @Override
    protected void paintComponent(Graphics g) {
        var.panelHeight = this.getHeight();
        var.panelWidth = this.getWidth();
        Font sanbold14 = new Font("Monospaced", Font.PLAIN, 48);
        g.setFont(sanbold14);
        g.drawString("" + countS, var.panelWidth / 2, var.panelHeight / 2);
        g.setColor(new Color(255, 0, 0));
        g.fillOval(var.x, var.y, var.ballWidth, var.ballHeight);
        g.setColor(new Color(0, 0, 255));
        g.fill3DRect(var.boardx, var.panelHeight - 15, 100, 15, true);
        blocks f = myflash.bb;
        for (int i = 0; i < f.getB().length; i++)
            for (int j = 0; j < f.getB()[0].length; j++) {
                if (f.getB()[i][j].getCurrentCondition()) {
                    g.setColor(f.getB()[0][0].getColor());
                    g.fill3DRect(f.getB()[i][j].getBlockLocationX(), f.getB()[i][j].getBlockLocationY(),
                            f.getB()[i][j].getBlockSideLength(), f.getB()[i][j].getBlockSideLength(), true);
                }
            }
    }
}

/*
The class is adapter that control the location of the board
two method (left and right)
*/
class myKeyListener extends KeyAdapter {
    static int tempSpeedx;
    static int tempSpeedy;

    @Override
    public void keyPressed(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.VK_LEFT || event.getKeyCode() == KeyEvent.VK_A)
            var.boardx = var.boardx - 15;
        else if (event.getKeyCode() == KeyEvent.VK_RIGHT || event.getKeyCode() == KeyEvent.VK_D)
            var.boardx = var.boardx + 15;
        else if (event.getKeyCode() == KeyEvent.VK_ENTER)
            myflash.speedCon(2, 4);
        else if (event.getKeyCode() == KeyEvent.VK_UP) {
            contiue();
        } else if (event.getKeyCode() == KeyEvent.VK_SPACE) {
            pause();
        }
        //����ʱʹ���Կ���С��ֱ���ƶ�
        else if (event.getKeyCode() == KeyEvent.VK_Z)
            var.y = var.y - 5;
        else if (event.getKeyCode() == KeyEvent.VK_X)
            var.x = var.x - 5;
        else if (event.getKeyCode() == KeyEvent.VK_C)
            var.y = var.y + 5;
        else if (event.getKeyCode() == KeyEvent.VK_V)
            var.x = var.x + 5;
    }

    /*
    ��ͣ����
     */
    static void pause() {
        tempSpeedx = var.speedx;
        tempSpeedy = var.speedy;
        myflash.speedCon(0, 0);
    }

    /*
    ��������
     */
    static void contiue() {
        myflash.speedCon(tempSpeedx, tempSpeedy);
    }
}

/*
this class abstract the blocks
 */
class blocks {
    private static int n;
    private int countx;
    private int county;
    private block[][] b;
    private int countBlocks;

    public blocks(int countx, int county) {
        this.countx = countx;
        this.county = county;
        n = var.panelWidth * 1 / 6 + 50;
        b = new block[countx][county];
        for (int i = 0; i < b.length; i++) {
            for (int j = 0; j < b[0].length; j++) {
                b[i][j] = new block(40, new Color(0, 255, 0));
                b[i][j].setBlockLocationX(n + b[i][j].getBlockSideLength() * (j - 1) + 100);
                b[i][j].setBlockLocationY(b[i][j].getBlockSideLength() * (i - 1) +
                        b[i][j].getBlockSideLength() + 60);
            }
        }
        countBlocks = countx * county;
    }

    public block[][] getB() {
        return b;
    }

    public void setCountBlocks(int countBlocks) {
        this.countBlocks = countBlocks;
    }

    public int getCountBlocks() {
        return countBlocks;
    }
}

class block {
    private int blockLocationX;
    private int blockLocationY;
    private int blockSideLength;
    private boolean currentCondition = true;
    private Color color;

    public block(int blockSideLength, Color color) {
        this.color = color;
        this.blockSideLength = blockSideLength;
    }

    public boolean getCurrentCondition() {
        return this.currentCondition;
    }

    public void setBlockLocationX(int blockLocationX) {
        this.blockLocationX = blockLocationX;
    }

    public void setBlockLocationY(int blockLocationY) {
        this.blockLocationY = blockLocationY;
    }

    public int getBlockSideLength() {
        return blockSideLength;
    }

    public int getBlockLocationX() {
        return blockLocationX;
    }

    public int getBlockLocationY() {
        return blockLocationY;
    }

    public Color getColor() {
        return color;
    }

    public void setCurrentCondition(boolean currentCondition) {
        this.currentCondition = currentCondition;
    }

}

/*
�ؿ��ĳ���
 */
class conmentsPass {
    private blocks[] myblocks;
    private int initRow = 3;//Ĭ������Ϊ3��
    private int initcolumn = 3;//��ʼĬ��Ϊ3��

    public conmentsPass() {
        myblocks = new blocks[10];
        for (int i = 0; i < myblocks.length; i++) {
            myblocks[i] = new blocks(initRow, initcolumn++);
        }
    }

    public blocks[] getMyblocks() {
        return myblocks;
    }
}

class keyBoard extends JFrame {
    public keyBoard() {
        setSize(var.screenWidth / 2, var.screenHeight - 50);
        setLocationRelativeTo(null);
        setVisible(true);
        setTitle("��ݼ�");
        init();
    }

    public void init() {
        JLabel jLabel = new JLabel("<html>" +
                "<body>" +
                "<h1>ENTER : ��ʼ</h1>" +
                "<h1>SCAPE : ��ͣ</h1>" +
                "<h1>UP : ����</h1>" +
                "<h1>LEFT : ��������</h1>" +
                "<h1>RIGHT : ��������</h1>" +
                "</html>");
        jLabel.setLayout(new FlowLayout());
        add(jLabel);
    }
}

class about extends JFrame {
    public about() {
        setSize(var.screenWidth / 2, var.screenHeight - 50);
        setLocationRelativeTo(null);
        setVisible(true);
        setTitle("����");
        init();
    }

    public void init() {
        JLabel jLabel = new JLabel("<html>" +
                "<body>" +
                "<h1>������ �����ݴ�ѧ2016���������1�����</h1>" +
                "<h1>����ʱ�� ��2017.06.14</h1>" +
                "<h1>�汾�� ��1.0</h1>" +
                "<h1>��ϵ���� ��guoliangxuanlan@outlook.com</h1>" +
                "<h1>ͼ�������http://www.easyicon.net/</h1>" +
                "</html>");
        jLabel.setLayout(new FlowLayout());
        add(jLabel);
    }
}

class explain extends JFrame {
    public explain() {
        setSize(var.screenWidth / 2, var.screenHeight - 50);
        setLocationRelativeTo(null);
        setVisible(true);
        setTitle("��Ϸ����");
        init();
    }

    public void init() {
        JLabel jLabel = new JLabel("<html>" +
                "<body>" +
                "<h1>�Ҵ�һJAVA�ε���ҵ." +
                "��С��Ϸ�Ǿ���ĵ����שС��Ϸ��ԭ����������ô��~o��o~" +
                "������10���ؿ���ÿ��һ���ؿ��ͻ��һ�С����У���Ϸ�����Ǹ����־��ǵ�ǰ�ؿ���." +
                "��ҵ�Ŀ������ܹ���ֵ�ÿһ�ص�ש�鶼����.</h1>" +
                "</html>");
        jLabel.setLayout(new FlowLayout());
        add(jLabel);
    }
}