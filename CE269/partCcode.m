K = 2.87;
Gc = tf([0 8], [1 2]);
Gsm = tf([0.117 0.0164 4.5709], [1 0.571 26.1033 1.2257]);
G = K*Gc*Gsm;
display(Gc);
display(Gsm);
display(G);
display(pole(G), 'poles');
display(zero(G), 'zeroes');
rlocus(G);
pos = 16;
z = -log(pos/100)/sqrt(pi^2+[log(pos/100)]^2);
display(z);
sgrid(z, 0);