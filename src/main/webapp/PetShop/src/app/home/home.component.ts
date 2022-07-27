import { ChangeDetectionStrategy, Component, OnInit } from '@angular/core';
import Swal from 'sweetalert2';

import { OwnerService } from '../owner/owner.service';
import { Subscription } from 'rxjs';
import { Router } from '@angular/router';
import { IOwner } from '../owner/owner';
import { IPet } from '../pet/pet';
import { PetService } from '../pet/pet.service';
import { BsDatepickerModule } from 'ngx-bootstrap/datepicker';
import { NgModel } from '@angular/forms';
import { DatePipe } from '@angular/common';
import { OwnerFunction } from '../owner/owner-funtions';
import { PetFunction } from '../pet/pet.function';


@Component({
  changeDetection: ChangeDetectionStrategy.OnPush,
  templateUrl: './home.component.html',
  providers: [DatePipe],
})
export class HomeComponent {
  pageTitle = 'Home Page';

  owner: IOwner | undefined;
  petList: IPet[] = [];
  ownerList: IOwner[] = [];

  sub!: Subscription;
  errorMessage: string = '';

  dateCreated: string = '';

  constructor(
    private ownerService: OwnerService,
    private petService: PetService,
    private router: Router,
    private datePipe: DatePipe,
    private ownerFunction: OwnerFunction,
    private petFunction : PetFunction
  ) {}

  // Handling Owner API
  owner_findById() {
    this.ownerFunction.owner_findById();
  }

  owner_findPetByOwnerId() {
    this.ownerFunction.owner_findPetByOwnerId();
  }

  owner_findByDateCreated(datePicker: NgModel) {
    this.dateCreated = this.datePipe.transform(datePicker.value, 'yyyy-MM-dd')!;
    this.ownerFunction.owner_findByDateCreated(this.dateCreated);
  }

  owner_findByPetId() {
    this.ownerFunction.owner_findByPetId();
  }

  owner_findByPetName() {
    this.ownerFunction.owner_findByPetName();
  }

  owner_create() {
    this.router.navigate(['/owner/add']);
  }

  // Handling Pet API
  pet_findById() {
    this.petFunction.pet_findById();
  }

  pet_create(){
    this.router.navigate(['/pet/add']);
  }

  displayError(errorMessage: string) {
    Swal.fire({
      icon: 'error',
      text: `${errorMessage}`,
    });
  }
}
