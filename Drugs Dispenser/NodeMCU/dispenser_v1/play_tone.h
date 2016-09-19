#ifndef __PLAY_TONE_H
#define __PLAY_TONE_H



struct song {

        int     _tone;
        short   _duration;

};


void playTone( int _pin, struct song *s, int n_size );
void beep (int BUZZER, float noteFrequency, long noteDuration);


#endif // __PLAY_TONE_H

