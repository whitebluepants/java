/**
 * Created by WhiteBlue Pants on 2017/9/11.
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener
{
    JTextField score;                                   //显示分数的标签和文本栏
    JLabel s;
    JLabel hint;                                        //提示暂停
    JLabel hint2;
    JProgressBar p;                                     //时间限制进度条
    Timer timer;
    JButton sos;                                        //停止或继续按钮
    JButton back;                                       //用来返回主菜单的按钮
    ImageIcon[] pattern = new ImageIcon[7];             //用来存放图标的数组 一共有7个
    int[][] patternArray = new int[8][8];               //用来记录每个按钮上的图标
    JButton[][] buttons = new JButton[8][8];            //游戏方盘为8行8列共64个按钮组成
    JPanel top;                                         //用来存放分数和进度条等的面板
    JPanel main;                                        //用来存放游戏内容
    Random rand = new Random();                         //用来随机生成已消除的图案
    int grade = 0;                                      //记录分数
    final int EMPTY = 0;                                //代表图案被消除后为空
    int x1, y1;                                         //用来记录第一次点击的按钮的坐标
    int x2, y2;                                         //用来记录第二次点击的按钮的坐标
    boolean isDoubleClicked;                            //判断是第一还是第二次点击按钮
    boolean isLinked;                                   //判断是否有三个以上
    boolean flag = true;                                //判断是该暂停还是开始

    GamePanel()
    {
        setLayout(null);
        initComponent();
        setSize(800, 600);
        setVisible(true);
    }

    public void initComponent()         //用来初始化控件
    {
        top = new JPanel();
        top.setLayout(new FlowLayout());
        top.setBounds(0, 0, 800, 50);
        main = new JPanel();
        main.setLayout(null);
        main.setBounds(0, 50, 800, 500);

        back = new JButton("返回主菜单");
        p = new JProgressBar(JProgressBar.HORIZONTAL, 0, 100);
        p.setValue(0);
        p.setStringPainted(true);
        score = new JTextField(10);
        score.setEditable(false);
        s = new JLabel("分数");

        for (int i = 0; i < 7; i++)                     //初始化每个图案
        {
            pattern[i] = new ImageIcon("images/" + (i + 1) + ".jpg");
        }

        for (int i = 0; i < 8; i++)                     //初始化每个按钮
        {
            for (int j = 0; j < 8; j++)
            {
                buttons[i][j] = new JButton();
                //buttons[i][j].setBorderPainted(false);
                buttons[i][j].setFocusable(false);
                buttons[i][j].setContentAreaFilled(false);
                buttons[i][j].setBounds(112 + 72 * i, 72 * j, 72, 72);
                buttons[i][j].addActionListener(this);

                main.add(buttons[i][j]);
            }
        }

        sos = new JButton("暂停/开始");
        sos.addActionListener(this);

        hint = new JLabel(">w<");
        hint.setFont(new java.awt.Font("STCAIYUN",1,30));
        hint.setBounds(1,30,100,100);
        hint.setForeground(Color.CYAN);
        hint.setVisible(false);
        hint2 = new JLabel("暂停中");
        hint2.setFont(new java.awt.Font("STCAIYUN",1,30));
        hint2.setBounds(1,1,100,100);
        hint2.setForeground(Color.CYAN);
        hint2.setVisible(false);

        top.add(back);
        top.add(sos);
        top.add(s);
        top.add(score);
        top.add(p);
        main.add(hint);
        main.add(hint2);

        add(main);
        add(top);
    }

    void stopOrStart()
    {

        if(flag)
        {
            hint.setVisible(true);
            hint2.setVisible(true);
            timer.stop();
            for(int i = 0;i < 8;i ++)
            {
                for(int j = 0;j < 8;j ++)
                {
                    buttons[i][j].setEnabled(false);
                    buttons[i][j].setOpaque(false);
                }
            }
            flag = false;
        }
        else
        {
            hint.setVisible(false);
            hint2.setVisible(false);
            timer.restart();
            for(int i = 0;i < 8;i ++)
            {
                for(int j = 0;j < 8;j ++)
                {
                    buttons[i][j].setEnabled(true);
                }
            }
            flag = true;
        }
    }

    void init()                                         //该函数用于开始游戏时初始化按钮图片
    {
        grade = 0;
        score.setText(Integer.toString(grade));
        do
        {
            initPattern();
        }while(search(1));
        updateIcon();
        timer.start();
        p.setValue(0);
    }

    void initPattern()
    {
        for (int i = 0; i < 8; i++)
        {
            for (int j = 0; j < 8; j++)
            {
                int m = (int) (Math.random() * 7);      // 随机生成一个在0-6范围的数字
                patternArray[i][j] = m;
            }
        }
    }

    boolean search(int flag)
    {
        if(flag == 1)
        {
            for(int i = 0;i < 8;i ++)
            {
                for(int j = 0;j < 8;j ++)
                {
                    if(isLinked(i,j))
                    {
                        return true;
                    }
                }
            }
        }
        else if(flag == 2)
        {
            for(int i = 0;i < 8;i ++)
            {
                for(int j = 0;j < 8;j ++)
                {
                    remove(i,j);
                }
            }
        }
        return false;
    }

    //该函数用来清除连接成功的图案
    void remove(int x,int y)
    {
        int n = 0;                                      //用来记录消除了几个 计算得分
        int linked = 1;

        //先判断y轴上是否有连击的

        //先判断y轴的上边
        if(x - 1 >= 0)
        {
            for(int i = x - 1;i >= 0;i --)
            {
                if(patternArray[x][y] == patternArray[i][y])
                {
                    linked ++;
                }
                else
                {
                    break;
                }
            }
        }
        //然后判断y轴的下边
        if(x + 1 < 8)
        {
            for(int i = x + 1;i < 8;i ++)
            {
                if(patternArray[x][y] == patternArray[i][y])
                {
                    linked ++;
                }
                else
                {
                    break;
                }
            }
        }
        //判断是否y轴是否有超过3个的 如果有就把每个按钮上的图案消去
        if(linked >= 3)
        {
            n = n + linked;
            for(int i = x - 1;i >= 0;i --)
            {
                if(patternArray[x][y] == patternArray[i][y])
                {
                    patternArray[i][y] = EMPTY;
                }
            }
            for(int i = x + 1;i < 8;i ++)
            {
                if(patternArray[x][y] == patternArray[i][y])
                {
                    patternArray[i][y] = EMPTY;
                }
            }
            patternArray[x][y] = EMPTY;
        }

        linked = 1;

        //然后判断x轴上是否有连击

        //先判断x轴的左边
        if(y - 1 >= 0)
        {
            for(int i = y - 1;i >= 0;i --)
            {
                if(patternArray[x][y] == patternArray[x][i])
                {
                    linked ++;
                }
                else
                {
                    break;
                }
            }
        }
        //然后判断x轴的右边
        if(y + 1 < 8)
        {
            for(int i = y + 1;i < 8;i ++)
            {
                if(patternArray[x][y] == patternArray[x][i])
                {
                    linked ++;
                }
                else
                {
                    break;
                }
            }
        }
        //判断是否x轴是否有超过3个的 如果有就把每个按钮上的图案消去
        if(linked >= 3)
        {
            n = n + linked;
            for(int i = y - 1;i >= 0;i --)
            {
                if(patternArray[x][y] == patternArray[x][i])
                {
                    patternArray[x][i] = EMPTY;
                }
            }
            for(int i = y + 1;i < 8;i ++)
            {
                if(patternArray[x][y] == patternArray[x][i])
                {
                    patternArray[x][i] = EMPTY;
                }
            }
            patternArray[x][y] = EMPTY;
        }

        grade += n * 10;
        score.setText(Integer.toString(grade));
    }

    //该函数用来判断是否有三个以上连接的
    boolean isLinked(int x,int y)
    {
        int linked = 1;

        // 判断y轴上是否有连击

        //先判断y轴上方
        if(x - 1 >= 0)
        {
            for(int i = x - 1;i >= 0;i --)
            {
                if(patternArray[i][y] == patternArray[x][y])
                {
                    linked ++;
                }
                else
                {
                    break;
                }
            }
        }
        //然后判断y轴下方
        if(x + 1 < 8)
        {
            for(int i = x + 1;i < 8;i ++)
            {
                if(patternArray[x][y] == patternArray[i][y])
                {
                    linked ++;
                }
                else
                {
                    break;
                }
            }
        }
        // 判断y轴上是否有连击
        if(linked >= 3)
        {
            return true;
        }

        linked = 1;

        // 判断x轴上是否有连击

        //先判断x轴的左边
        if(y - 1 >= 0)
        {
            for(int i = y - 1;i >= 0;i --)
            {
                if(patternArray[x][y] == patternArray[x][i])
                {
                    linked ++;
                }
                else
                {
                    break;
                }
            }
        }
        //然后判断x轴的右边
        if(y + 1 < 8)
        {
            for(int i = y + 1;i < 8;i ++)
            {
                if(patternArray[x][y] == patternArray[x][i])
                {
                    linked ++;
                }
                else
                {
                    break;
                }
            }
        }
        // 判断x轴上是否有连击
        if(linked >= 3)
        {
            return true;
        }
        return false;
    }

    //该函数用来更新被消除的图案
    void update()
    {
        for(int i = 0;i < 8;i ++)
        {
            for(int j = 0;j < 8;j ++)
            {
                if(patternArray[i][j] == EMPTY)
                {
                    patternArray[i][j] = rand.nextInt(7);
                }
            }
        }
    }

    //更新图案被消掉后下降下来
    void down()
    {
        int temp;
        for(int j = 7;j >= 0;j --)
        {
            for(int i = 0;i < 8;i ++)
            {
                if(patternArray[j][i] == EMPTY)
                {
                    for(int k = j - 1;k >= 0;k --)
                    {
                        if(patternArray[k][i] != EMPTY)
                        {
                            temp = patternArray[k][i];
                            patternArray[k][i] = patternArray[j][i];
                            patternArray[j][i] = temp;
                            break;
                        }
                    }
                }
            }
        }
    }

    //把图案显示在按钮上
    void updateIcon()
    {
        for(int i = 0;i < 8;i ++)
        {
            for(int j = 0;j < 8;j ++)
            {
                buttons[i][j].setIcon(pattern[patternArray[i][j]]);
            }
        }
    }

    //交换两个按钮的图标
    void swap(int x,int y)
    {
        int temp;
        if(!isDoubleClicked)
        {
            x1 = x;
            y1 = y;
            isDoubleClicked = true;
        }
        else
        {
            x2 = x;
            y2 = y;
            isDoubleClicked = false;
            if(Math.abs( (x1 - x2) ) == 1 && y1 == y2 || Math.abs(y1 - y2) == 1 && x1 == x2)
            {
                temp = patternArray[x1][y1];
                patternArray[x1][y1] = patternArray[x2][y2];
                patternArray[x2][y2] = temp;

                 if(isLinked(x1,y1) || isLinked(x2,y2))
                 {
                     if (isLinked(x1, y1))
                     {
                         remove(x1, y1);
                     }
                     if (isLinked(x2, y2))
                     {
                         remove(x2, y2);
                     }
                    down();
                    update();
                    updateIcon();

                    while(search(1))
                    {
                        search(2);
                        down();
                        update();
                        updateIcon();
                    }
                }
                else                                    //交换后没有连线 交换回去
                {
                    temp = patternArray[x1][y1];
                    patternArray[x1][y1] = patternArray[x2][y2];
                    patternArray[x2][y2] = temp;
                    updateIcon();
                }
            }
        }
    }

    public void actionPerformed(ActionEvent e)
    {
        if(e.getSource() == sos)
        {
            stopOrStart();
        }
        for(int i = 0;i < 8;i ++)
        {
            for(int j = 0;j < 8;j ++)
            {
                if(buttons[i][j] == e.getSource())
                {
                    swap(i,j);
                }
            }
        }
    }
}
