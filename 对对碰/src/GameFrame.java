/**
 * Created by WhiteBlue Pants on 2017/9/6.
 */
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class GameFrame extends JFrame implements ActionListener{
    ImageIcon image;
    JLabel bg;
    MenuPanel menupanel;        //用来存放按钮控件的一层panel
    GamePanel gamepanel;        //游戏时的panel
    int w = 800, h = 600;       //窗口的长和宽
    TimerListener time = new TimerListener();
    GameFrame()
    {
        super("对对碰>.<");
        initComponent();
        setContentPane(menupanel);
        getLayeredPane().add(bg,new Integer(Integer.MIN_VALUE));
        setSize(w,h);
        setVisible(true);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void initComponent()  // 用来初始化控件的函数
    {
        image = new ImageIcon("images/background.png");  //背景图片初始化
        bg = new JLabel(image);
        bg.setBounds(0,0,image.getIconWidth(),image.getIconHeight());

        gamepanel = new GamePanel();        //初始化游戏面板并添加到窗口
        add(gamepanel);
        menupanel = new MenuPanel();        //初始化菜单面板并添加到窗口
        add(menupanel);

        GameaddListener();                  //为游戏面板的按钮添加监听器
        MenuaddListener();                  //为菜单面板的按钮添加监听器
    }

    public void MenuaddListener()
    {
        menupanel.exit.addActionListener(this);
        menupanel.startGame.addActionListener(this);
    }
    public void GameaddListener()
    {
        gamepanel.timer = new Timer(800,time);
        gamepanel.back.addActionListener(this);

    }

    class TimerListener implements  ActionListener      // GamePanel的timer的监听器
    {
        int times = 0;
        public void actionPerformed(ActionEvent e)
        {
            gamepanel.p.setValue((times ++));
            if(times > 100)
            {
                gamepanel.timer.stop();
                String msg = "时间到，是否回到主菜单";
                int type = JOptionPane.YES_NO_OPTION;
                int choice = JOptionPane.showConfirmDialog(gamepanel,msg,"游戏结束",type);
                if(choice == 1)
                {
                    times = 0;
                    gamepanel.init();
                }
                else if(choice == 0)
                {
                    setContentPane(menupanel);
                }
            }
        }
    }

    public void actionPerformed(ActionEvent e)
    {
        String s = e.getSource().toString();
        if (s.equals(menupanel.exit.toString()))
        {
            System.exit(0);
        }
        else if (s.equals(menupanel.startGame.toString()))
        {
            time.times = 0;
            gamepanel.init();
            setContentPane(gamepanel);
        }
        else if (s.equals(gamepanel.back.toString()))
        {
            gamepanel.timer.stop();
            setContentPane(menupanel);
        }
    }
    public static void main(String [] args)
    {
        GameFrame a = new GameFrame();
    }
}
