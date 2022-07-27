import { Component } from '@angular/core';

@Component({
  selector: 'app-root',
  template: `
    <nav class="navbar navbar-expand-lg navbar-light bg-light">
      <a class="navbar-brand m-3" routerLink="/home">{{pageTitle}}</a>
      <div class="collapse navbar-collapse" id="navbarNav">
        <ul class="navbar-nav">
          <li class="nav-item">
            <a class="nav-link" routerLink="/owner/findAll">Owners</a>
          </li>
          <li class="nav-item">
            <a class="nav-link" routerLink="/pet/findAll" href="#">Pets</a>
          </li>
        </ul>
      </div>
    </nav>
    <div class="container-fluid">
      <router-outlet></router-outlet>
    </div>
  `,
})
export class AppComponent {
  pageTitle: string = 'Angular Pet Shop';
}
