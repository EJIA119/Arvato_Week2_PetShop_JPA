import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { RouterModule } from '@angular/router';

import { HttpClientModule } from '@angular/common/http';

import { AppComponent } from './app.component';
import { OwnerComponent } from './owner/owner.component';
import { HomeComponent } from './home/home.component';
import { PetComponent } from './pet/pet.component';
import { OwnerDetailComponent } from './owner/detail/owner-detail.component';
import { OwnerCreateComponent } from './owner/create/owner-create.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { PetUpdateComponent } from './pet/update/pet-update/pet-update.component';
import { PetDetailComponent } from './pet/pet-detail/pet-detail.component';

import { BsDatepickerModule } from 'ngx-bootstrap/datepicker';

import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { TopNameComponent } from './pet/top-name/top-name.component';
import { CreateComponent } from './pet/create/create.component';


@NgModule({
  declarations: [
    AppComponent,
    HomeComponent,
    OwnerComponent,
    PetComponent,
    OwnerDetailComponent,
    OwnerCreateComponent,
    PetUpdateComponent,
    PetDetailComponent,
    TopNameComponent,
    CreateComponent,
    
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    RouterModule.forRoot([
      { path : 'home', component: HomeComponent },
      { path : '', component: HomeComponent },
      { path : 'owner/findAll', component: OwnerComponent },
      { path : 'owner/findById/:id', component: OwnerDetailComponent },
      { path : 'owner/findByDateCreated/:dateCreated', component: OwnerComponent },
      { path : 'owner/findByPetName/:name', component: OwnerComponent },
      { path : 'owner/findByPetId/:id', component: OwnerDetailComponent },
      { path : 'owner/add', component: OwnerCreateComponent },
      
      { path : 'pet/add', component: CreateComponent },
      { path : 'pet/findAll', component: PetComponent },
      { path : 'pet/findById/:id', component: PetDetailComponent },
      { path : 'pet/update/:id', component: PetUpdateComponent },
      { path : 'pet/topName', component: TopNameComponent}
    ]),
    ReactiveFormsModule,
    BrowserAnimationsModule,
    BsDatepickerModule.forRoot(),
    FormsModule,
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
