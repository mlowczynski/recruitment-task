import { Component, OnInit } from '@angular/core';
import { HeroService, EnemyService } from '../shared';

@Component({
  selector: 'app-hero-board',
  templateUrl: './hero-board.component.html',
  styleUrls: ['./hero-board.component.css']
})
export class HeroBoardComponent implements OnInit {
  heroes: any[] = [];
  enemies: any[] = [];
  edited: boolean = false;
  isHeroService: boolean = true;
  heroId: number = -1;
  enemyId: number = -1;

  constructor(private heroService: HeroService, private enemyService: EnemyService) { }

  ngOnInit(): void {
    this.getAllHeroes();

    let tables = document.querySelectorAll('table');
    let headers = document.querySelectorAll('.heroheader, .enemyheader');
    let searchInput = document.querySelector('.search');
    let addbutton = document.querySelector('.add');
    let removebutton = document.querySelector('.remove');

    tables.forEach((table) => {
      table.addEventListener('click', () => {
        this.isHeroService = table.getAttribute('class')?.match('hero') != null;
      });
    });

    addbutton?.addEventListener('click', () => {
      this.addRecord();
    });

    removebutton?.addEventListener('click', () => {
      this.removeRecord();
    });

    searchInput?.addEventListener('input', () => {
      let keyword = (<HTMLInputElement>searchInput)?.value
      if (keyword != "") {
        this.search((<HTMLInputElement>searchInput)?.value)
      }
      else {
        this.getAllHeroes();
        this.enemies = [];
      }
    })

    headers.forEach((header) => {
      header?.addEventListener('mouseover', () => {
        if (header.getAttribute('class')?.match('hero') != null)
          if (tables[0]?.childElementCount == 1)
            header?.setAttribute('contenteditable', "true");
          else
            header?.setAttribute('contenteditable', "false");
        else
          if (tables[1]?.childElementCount == 1 && this.heroId != -1)
            header?.setAttribute('contenteditable', "true");
          else
            header?.setAttribute('contenteditable', "false");
      });
    });
  }

  preventEnter(e: KeyboardEvent): void {
    if (e.key === 'Enter')
      e.preventDefault();
  }

  search(keyword: string) {
    this.heroService.searchHeroes(keyword).subscribe(
      data => {
        this.heroes = data
      },
      error => console.log(error)
    )
    this.enemyService.searchEnemies(keyword).subscribe(
      data => {
        this.enemies = data
      },
      error => console.log(error)
    )
  }

  getAllHeroes(): void {
    this.heroService.getAllHeroes().subscribe(
      data => {
        this.heroes = data;
      },
      error => console.log(error)
    );
  }

  showHeroDetails(): void {
    let searchInput = <HTMLInputElement>document.querySelector('.search')
    if (searchInput.value != '') {
      searchInput.value = '';
      this.getAllHeroes();
    }
    this.heroService.getHero(this.heroId).subscribe(
      data => {
        this.enemies = data.enemiesDto;
      },
      error => console.log(error)
    );
  }

  showEnemyDetails(): void {
    let searchInput = <HTMLInputElement>document.querySelector('.search')
    if (searchInput.value != '') {
      this.showHeroDetails();
    }
  }

  getAllEnemies(): void {
    this.enemyService.getAllEnemies().subscribe(
      data => {
        this.enemies = data;
      },
      error => console.log(error)
    );
  }

  updateRecord(tr: ChildNode): void {
    if (this.edited) {
      this.edited = false;
      let id = (<HTMLElement>tr.childNodes[0]).innerHTML;
      let name = (<HTMLElement>tr.childNodes[1].childNodes[1]).innerHTML;
      if (this.isHeroService)
        this.heroService.editHero(+id, {'name': name}).subscribe(
          () => {},
          error => console.log(error)
        );
      else 
        this.enemyService.editEnemy(+id, {'name': name}).subscribe(
          () => {},
          error => console.log(error)
        );
    }
  }

  addRecord(): void {
    if (this.isHeroService) {
      this.heroService.addHero({'name': 'new hero'}).subscribe(
        data => {
          this.heroes.push(data);
        },
        error => console.log(error)
      );
    } else {
      if (this.heroId === -1)
        return;
      this.enemyService.addEnemy({'name': 'new enemy'}).subscribe(
        data => {
          this.heroService.addEnemyToHero(this.heroId, data.id).subscribe(
            data => {
              this.enemies = data.enemiesDto;
            },
            error => console.log(error)
          );
        },
        error => console.log(error)
      );
    }
  }

  removeRecord(): void {
    if (this.isHeroService) {
      if (this.heroId === -1)
        return;
      this.heroService.deleteHero(this.heroId).subscribe(
        data => {
          for (let i = 0; i < this.heroes.length; ++i) {
            if (this.heroes[i].id === data.id) {
              this.heroes.splice(i, 1);
              this.enemies = [];
              break;
            }
          }
        },
        error => console.log(error)
      );
      this.heroId = -1;
    } else {
      if (this.enemyId === -1 || this.heroId === -1)
        return;
      this.enemyService.deleteEnemy(this.enemyId).subscribe(
        data => {
          for (let i = 0; i < this.enemies.length; ++i) {
            if (this.enemies[i].id === data.id) {
              this.enemies.splice(i, 1);
              break;
            }
          }
        },
        error => console.log(error)
      );
      this.enemyId = -1;
    }
  }
}
