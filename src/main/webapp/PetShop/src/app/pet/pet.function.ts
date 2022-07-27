import { Injectable } from '@angular/core';
import { Subscription } from 'rxjs';
import { IPet } from './pet';
import { PetService } from './pet.service';
import Swal from 'sweetalert2';
import { Router } from '@angular/router';
import * as $ from 'jquery';
import { DisplayMessage } from '../shared/display-message';

@Injectable({
  providedIn: 'root',
})
export class PetFunction {
  petList: IPet[] = [];
  pet: IPet | undefined;

  sub!: Subscription;
  errorMessage: string = '';

  constructor(private petService: PetService, private router: Router, private dm: DisplayMessage) {}

  // Decide which path to go
  // 1 - find by id

  pet_findById() {
    Swal.fire({
      title: 'Find by Pet ID',
      html: "<input type='text' class='form-control' id='petID' />",
      confirmButtonText: 'Find',
      showCloseButton: true,
      preConfirm: () => {
        let id = String($('#petID').val());

        if (!id) Swal.showValidationMessage('Please enter Pet ID to find.');

        return { petID: id };
      },
    }).then((result) => {
      if (result.isConfirmed) {
        this.sub = this.petService
          .findById(Number(result.value!.petID))
          .subscribe({
            next: (pet) => {
              this.router.navigate([`/pet/findById/${result.value!.petID}`]);
            },
            error: (err) => {
              this.errorMessage = err;
              this.dm.displayError(err);
            },
            complete: () => {},
          });
      }
    });
  }

}
