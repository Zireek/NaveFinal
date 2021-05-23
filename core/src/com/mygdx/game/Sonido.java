
package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

public class Sonido {
    public Sound disparo = Gdx.audio.newSound(Gdx.files.internal("disparo1.mp3"));
    public Sound enemigoexplosion = Gdx.audio.newSound(Gdx.files.internal("explosionenemigo.mp3"));
    public Sound explosion = Gdx.audio.newSound(Gdx.files.internal("explosion1.mp3"));
    public Music musica = Gdx.audio.newMusic(Gdx.files.internal("musicafondo.mp3"));
}