for re = -0.2:0.2:0.2
    for im = -1:1
        c = re + im*j;
        t = 0:0.01:20;
        x = exp(c*t);
        plot(t, real(x), t, imag(x)), grid;
        hold on;
    end
end
