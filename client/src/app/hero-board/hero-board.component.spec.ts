import { ComponentFixture, TestBed } from '@angular/core/testing';

import { HeroBoardComponent } from './hero-board.component';

describe('HeroBoardComponent', () => {
  let component: HeroBoardComponent;
  let fixture: ComponentFixture<HeroBoardComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ HeroBoardComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(HeroBoardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
