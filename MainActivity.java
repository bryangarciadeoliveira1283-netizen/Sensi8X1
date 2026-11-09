package com.sensi8x.app;

import android.app.*;
import android.os.*;
import android.content.*;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.view.*;
import android.widget.*;

public class MainActivity extends Activity {
  final int BG=0xff101014, PANEL=0xff1d1d23, ORANGE=0xffff8b23, WHITE=0xfff6f6f8;
  LinearLayout root, content; android.content.SharedPreferences prefs;
  int sens=150, dpi=420; String profile="Equilibrado";
  GradientDrawable shape(int color,int stroke){GradientDrawable d=new GradientDrawable();d.setColor(color);d.setCornerRadius(22);if(stroke!=0)d.setStroke(2,stroke);return d;}
  TextView text(String s,int sp,int color){TextView t=new TextView(this);t.setText(s);t.setTextSize(sp);t.setTextColor(color);t.setPadding(10,8,10,8);return t;}
  LinearLayout section(String title){LinearLayout b=new LinearLayout(this);b.setOrientation(1);b.setPadding(18,16,18,16);b.setBackground(shape(PANEL,0xff393039));LinearLayout.LayoutParams p=new LinearLayout.LayoutParams(-1,-2);p.setMargins(0,10,0,10);content.addView(b,p);TextView h=text(title,20,ORANGE);h.setTypeface(null,Typeface.BOLD);b.addView(h);return b;}
  void line(LinearLayout b,String s){b.addView(text(s,14,WHITE));}
  void button(LinearLayout b,String s,Runnable action){Button bt=new Button(this);bt.setAllCaps(false);bt.setText(s);bt.setTextColor(WHITE);bt.setBackground(shape(0xff343039,ORANGE));LinearLayout.LayoutParams p=new LinearLayout.LayoutParams(-1,-2);p.setMargins(0,6,0,6);b.addView(bt,p);bt.setOnClickListener(v->action.run());}
  void slider(LinearLayout b,String key,String label,int max,int start){TextView caption=text(label+": "+prefs.getInt(key,start),15,WHITE);b.addView(caption);SeekBar bar=new SeekBar(this);bar.setMax(max);bar.setProgress(prefs.getInt(key,start));b.addView(bar);bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){public void onStartTrackingTouch(SeekBar s){}public void onStopTrackingTouch(SeekBar s){}public void onProgressChanged(SeekBar s,int val,boolean user){caption.setText(label+": "+val);if(user)prefs.edit().putInt(key,val).apply();}});}
  void note(LinearLayout b,String s){TextView t=text(s,13,0xffb5b1b8);b.addView(t);}
  public void onCreate(Bundle b){super.onCreate(b);prefs=getSharedPreferences("sensi8x",MODE_PRIVATE);show();}
  void show(){ScrollView sc=new ScrollView(this);sc.setFillViewport(true);sc.setBackgroundColor(BG);root=new LinearLayout(this);root.setOrientation(1);root.setPadding(18,20,18,24);sc.addView(root);setContentView(sc);TextView head=text("SENSI 8X",30,ORANGE);head.setTypeface(null,Typeface.BOLD);root.addView(head);root.addView(text("Perfil Galaxy A17 • calibração manual para Free Fire",14,WHITE));content=root;
    LinearLayout s=section("8XTrick • Sensibilidade");
    slider(s,"geral","Geral",200,150);slider(s,"red","Ponto vermelho",200,145);slider(s,"scope2","Mira 2x",200,130);slider(s,"scope4","Mira 4x",200,115);slider(s,"sniper","AWM",200,70);slider(s,"free","Olhadinha",200,140);
    button(s,"Galaxy A17 • Equilibrado",()->preset(150,145,130,115,70,140));button(s,"Galaxy A17 • Puxada rápida",()->preset(185,175,160,145,85,170));button(s,"Galaxy A17 • Controle preciso",()->preset(125,120,110,95,55,120));
    note(s,"Copie os valores manualmente nas configurações do jogo. Não altera arquivos ou memória do Free Fire.");
    s=section("Boneco Lento • Controle de puxada");
    line(s,"Treino de arrasto: faça puxadas curtas e consistentes, ajustando a Geral em passos de 5 pontos.");
    button(s,"A mira passa da cabeça",()->adjust(-5));button(s,"A mira fica no peito",()->adjust(5));
    note(s,"Esses botões recalibram sua recomendação de Geral. Não alteram a velocidade dos jogadores.");
    s=section("8XTrick • Mira vermelha e precisão");
    line(s,"Fanshoot Sensibilidade: calibração da mira para puxadas controladas.");
    button(s,"Aumentar precisão • diminuir Ponto Vermelho",()->{change("red",-5);show();});
    button(s,"Puxada mais rápida • aumentar Ponto Vermelho",()->{change("red",5);show();});
    s=section("Reduzir Recuo • calibração manual");
    line(s,"Use rajadas curtas e teste a sensibilidade da mira 2x/4x no campo de treino.");
    button(s,"Diminuir 2x e 4x em 5 pontos",()->{change("scope2",-5);change("scope4",-5);show();});
    button(s,"Aumentar 2x e 4x em 5 pontos",()->{change("scope2",5);change("scope4",5);show();});
    s=section("Retirar Input Lag • Ajustes do aparelho");
    line(s,"Feche apps pesados, use a taxa de atualização disponível no aparelho e desative economia de bateria durante o treino.");
    button(s,"Abrir configurações de tela",()->{try{startActivity(new Intent(android.provider.Settings.ACTION_DISPLAY_SETTINGS));}catch(Exception e){toast("Configuração indisponível");}});
    s=section("Taxa de Quadros • FPS Boost");
    float hz=getWindowManager().getDefaultDisplay().getRefreshRate();line(s,"Taxa de atualização detectada: "+Math.round(hz)+" Hz");
    line(s,"120 / 240 / 360 FPS: valores de referência. Este app não desbloqueia FPS do jogo.");
    s=section("Hz Boost • 60 / 90 / 120 / 144 / 240 Hz");line(s,"A frequência física da tela não pode ser aumentada por este painel.");button(s,"Abrir configurações de atualização",()->{try{startActivity(new Intent(android.provider.Settings.ACTION_DISPLAY_SETTINGS));}catch(Exception e){toast("Configuração indisponível");}});
    s=section("Salvar e copiar • Perfil Galaxy A17");button(s,"Copiar todas as sensibilidades",()->{String msg="Sensi 8X: Geral "+get("geral",150)+", Ponto vermelho "+get("red",145)+", 2x "+get("scope2",130)+", 4x "+get("scope4",115)+", AWM "+get("sniper",70)+", Olhadinha "+get("free",140);((android.content.ClipboardManager)getSystemService(CLIPBOARD_SERVICE)).setPrimaryClip(ClipData.newPlainText("Sensi 8X",msg));toast("Valores copiados");});
  }
  int get(String k,int def){return prefs.getInt(k,def);} void change(String k,int d){prefs.edit().putInt(k,Math.max(0,Math.min(200,get(k,100)+d))).apply();}
  void adjust(int d){change("geral",d);show();toast("Geral ajustada em "+d+" pontos");}
  void preset(int a,int b,int c,int d,int e,int f){prefs.edit().putInt("geral",a).putInt("red",b).putInt("scope2",c).putInt("scope4",d).putInt("sniper",e).putInt("free",f).apply();show();toast("Perfil aplicado ao painel");}
  void toast(String s){Toast.makeText(this,s,Toast.LENGTH_SHORT).show();}
}
