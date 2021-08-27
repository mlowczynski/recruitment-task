import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppComponent } from './app.component';
import { HeroBoardComponent } from './hero-board/hero-board.component';

import { HttpClientModule } from '@angular/common/http';
import { HeroService, EnemyService } from './shared';

@NgModule({
  declarations: [
    AppComponent,
    HeroBoardComponent
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
  ],
  providers: [HeroService, EnemyService],
  bootstrap: [AppComponent]
})
export class AppModule { }
