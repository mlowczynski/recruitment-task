import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable()
export class HeroService {

  constructor(private http: HttpClient) {}

  getAllHeroes(): Observable<any> {
    return this.http.get('http://localhost:8080/heroes');
  }

  searchHeroes(keyword: string): Observable<any> {
    return this.http.get('http://localhost:8080/heroes/search', { params: { 'q' : keyword } });
  }

  getHero(id: number): Observable<any> {
    return this.http.get('http://localhost:8080/heroes/' + id);
  }

  editHero(id: number, body: any): Observable<any> {
    return this.http.put('http://localhost:8080/heroes/' + id, body);
  }

  addHero(body: any): Observable<any> {
    return this.http.post('http://localhost:8080/heroes/', body);
  }

  deleteHero(id: number): Observable<any> {
    return this.http.delete('http://localhost:8080/heroes/' + id);
  }

  addEnemyToHero(heroId: number, enemyId: number): Observable<any> {
    return this.http.post('http://localhost:8080/heroes/' + heroId + "/enemies/" + enemyId + "/add", {});
  }

  removeEnemyFromHero(heroId: number, enemyId: number): Observable<any> {
    return this.http.delete('http://localhost:8080/heroes/' + heroId + "/enemies/" + enemyId + "/remove", {});
  }
}
