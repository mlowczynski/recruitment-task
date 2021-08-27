import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable()
export class EnemyService {

  constructor(private http: HttpClient) {}

  getAllEnemies(): Observable<any> {
    return this.http.get('http://localhost:8080/enemies');
  }

  searchEnemies(keyword: string): Observable<any> {
    return this.http.get('http://localhost:8080/enemies/search', { params: { 'q' : keyword } });
  }

  getEnemy(id: number): Observable<any> {
    return this.http.get('http://localhost:8080/enemies/' + id);
  }

  addEnemy(body: any): Observable<any> {
    return this.http.post('http://localhost:8080/enemies/', body);
  }

  editEnemy(id: number, body: any): Observable<any> {
    return this.http.put('http://localhost:8080/enemies/' + id, body);
  }

  deleteEnemy(id: number): Observable<any> {
    return this.http.delete('http://localhost:8080/enemies/' + id);
  }
}
