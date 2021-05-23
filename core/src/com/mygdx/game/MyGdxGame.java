package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.ArrayList;
import java.util.List;

public class MyGdxGame extends ApplicationAdapter {
    SpriteBatch batch;
    BitmapFont font;
    Fondo fondo;
    Jugador jugador;
    List<Enemigo> enemigos;
    List<Enemigo2> enemigos2;
    List<Disparo> disparosAEliminar;
    List<Enemigo> enemigosAEliminar;
    Temporizador temporizadorNuevoEnemigo;
    List<Enemigo2> enemigos2AEliminar;
    Temporizador temporizadorNuevoEnemigo2;
    ScoreBoard scoreboard;
    Sonido sonido;
    boolean gameover;


    @Override
    public void create() {
        batch = new SpriteBatch();
        font = new BitmapFont();
        font.setColor(Color.WHITE);
        font.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        font.getData().setScale(2f);

        inicializarJuego();
    }

    void inicializarJuego(){
        fondo = new Fondo();
        jugador = new Jugador();
        enemigos = new ArrayList<>();
        enemigos2 = new ArrayList<>();
        temporizadorNuevoEnemigo = new Temporizador(120);
        temporizadorNuevoEnemigo2 = new Temporizador(120);
        disparosAEliminar = new ArrayList<>();
        enemigosAEliminar = new ArrayList<>();
        enemigos2AEliminar = new ArrayList<>();
        scoreboard = new ScoreBoard();
        sonido = new Sonido();
        sonido.musica.play();

        gameover = false;
    }

    void update() {
        Temporizador.framesJuego += 1;

        if (temporizadorNuevoEnemigo.suena()) enemigos.add(new Enemigo());
        if (temporizadorNuevoEnemigo2.suena()) enemigos2.add(new Enemigo2());

        if(!gameover) jugador.update();

        for (Enemigo enemigo : enemigos) enemigo.update();
        for (Enemigo2 enemigo2 : enemigos2) enemigo2.update();

        for (Enemigo enemigo : enemigos) {
            for (Disparo disparo : jugador.disparos) {
                if (Utils.solapan(disparo.x, disparo.y, disparo.w, disparo.h, enemigo.x, enemigo.y, enemigo.w, enemigo.h)) {
                    disparosAEliminar.add(disparo);
                    enemigosAEliminar.add(enemigo);
                    jugador.puntos++;
                    sonido.enemigoexplosion.play();
                    break;
                }
            }

            if (!gameover && !jugador.muerto && Utils.solapan(enemigo.x, enemigo.y, enemigo.w, enemigo.h, jugador.x, jugador.y, jugador.w, jugador.h)) {
                jugador.morir();
                if (jugador.vidas == 0){
                    sonido.explosion.play();
                    gameover = true;
                }
            }

            if (enemigo.x < -enemigo.w) enemigosAEliminar.add(enemigo);
            if (enemigo.y < 0) enemigo.y = 720 ;
            if (enemigo.y > 720) enemigo.y = 0 ;
        }
        for (Enemigo2 enemigo2 : enemigos2) {
            for (Disparo disparo : jugador.disparos) {
                if (Utils.solapan(disparo.x, disparo.y, disparo.w, disparo.h, enemigo2.x, enemigo2.y, enemigo2.w, enemigo2.h)) {
                    disparosAEliminar.add(disparo);
                    enemigos2AEliminar.add(enemigo2);
                    jugador.puntos++;
                    sonido.enemigoexplosion.play();
                    break;
                }
            }

            if (!gameover && !jugador.muerto && Utils.solapan(enemigo2.x, enemigo2.y, enemigo2.w, enemigo2.h, jugador.x, jugador.y, jugador.w, jugador.h)) {
                jugador.morir();
                if (jugador.vidas == 0){
                    sonido.explosion.play();
                    gameover = true;
                }
            }

            if (enemigo2.x < -enemigo2.w) enemigos2AEliminar.add(enemigo2);
            if (enemigo2.y < 0) enemigo2.y = 720 ;
            if (enemigo2.y > 720) enemigo2.y = 0 ;
        }

        for (Disparo disparo : jugador.disparos)
            if (disparo.x > 1280)
                disparosAEliminar.add(disparo);

        for (Disparo disparo : disparosAEliminar) jugador.disparos.remove(disparo);
        for (Enemigo enemigo : enemigosAEliminar) enemigos.remove(enemigo);
        for (Enemigo2 enemigo2 : enemigos2AEliminar) enemigos2.remove(enemigo2);
        disparosAEliminar.clear();
        enemigosAEliminar.clear();
        enemigos2AEliminar.clear();

        if(gameover) {
            int result = scoreboard.update(jugador.puntos);
            if(result == 1) {
                inicializarJuego();
            } else if (result == 2) {
                Gdx.app.exit();
            }
            sonido.musica.stop();
        }


    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        update();

        batch.begin();
        fondo.render(batch);
        jugador.render(batch);

        for (Enemigo enemigo : enemigos) enemigo.render(batch);
        for (Enemigo2 enemigo2 : enemigos2) enemigo2.render(batch);
        font.draw(batch, "VIDAS: " + jugador.vidas, 30, 700);
        font.draw(batch, "PUNTOS: " + jugador.puntos, 30, 660);

        if (gameover){
            scoreboard.render(batch, font);
        }
        batch.end();
    }
}