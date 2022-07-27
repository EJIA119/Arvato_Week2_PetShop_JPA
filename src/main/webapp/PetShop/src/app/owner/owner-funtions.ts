import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { Subscription } from 'rxjs';
import Swal from 'sweetalert2';
import { IPet } from '../pet/pet';
import { IOwner } from './owner';
import { OwnerService } from './owner.service';
import * as $ from 'jquery';
import { NgModel } from '@angular/forms';
import { DisplayMessage } from '../shared/display-message';

@Injectable({
  providedIn: 'root',
})
export class OwnerFunction {
  owner!: IOwner | undefined;
  petList: IPet[] = [];
  ownerList: IOwner[] = [];

  sub!: Subscription;
  errorMessage: string = '';

  constructor(private ownerService: OwnerService, private router: Router, private dm: DisplayMessage) {}

  owner_findById() {
    Swal.fire({
      title: 'Find Owner by ID',
      html: "<input type='number' min='1' class='form-control' id='ownerID'>",
      confirmButtonText: 'Find',
      showCloseButton: true,
      preConfirm: () => {
        let id = $('#ownerID').val();

        if (!id) Swal.showValidationMessage('Please enter Owner ID to find.');

        return { ownerID: id! };
      },
    }).then((result) => {
      if (result.isConfirmed) {
        this.sub = this.ownerService
          .findById(Number(result.value!.ownerID))
          .subscribe({
            next: (owner) => {
              this.owner = owner;
            },
            error: (err) => {
              (this.errorMessage = err), this.dm.displayError(this.errorMessage);
            },
            complete: () => {
              this.router.navigate([
                `/owner/findById/${result.value?.ownerID}`,
              ]);
            },
          });
      }
    });
  }

  owner_findPetByOwnerId() {
    Swal.fire({
      title: 'Find Pet by Owner ID',
      html: "<input type='number' id='ownerID' class='form-control'/>",
      confirmButtonText: 'Find',
      showCloseButton: true,
      preConfirm: () => {
        let id = $('#ownerID').val();

        if (!id) Swal.showValidationMessage('Please enter Owner ID to find.');

        return { ownerID: id };
      },
    }).then((result) => {
      if (result.isConfirmed) {
        this.sub = this.ownerService
          .findPetByOwnerId(Number(result.value!.ownerID))
          .subscribe({
            next: (petList) => {
              this.petList = petList;
            },
            error: (err) => {
              this.dm.displayError(err);
            },
            complete: () => {
              if (this.petList.length > 0) {
                this.router.navigate([
                  `/owner/findById/${result.value!.ownerID}`,
                ]);
              }
            },
          });
      }
    });
  }

  owner_findByDateCreated(dateCreated: string) {
    this.sub = this.ownerService.findByDateCreated(dateCreated).subscribe({
      next: (ownerList) => {
        this.ownerList = ownerList;
      },
      error: (err) => {
        this.dm.displayError(err);
      },
      complete: () => {
        if (this.ownerList.length > 0) {
          this.router.routeReuseStrategy.shouldReuseRoute = () => false;
          this.router.onSameUrlNavigation = 'reload';
          this.router.navigate([`/owner/findByDateCreated/${dateCreated}`]);
        } else
          this.dm.displayError(
            `No Owner record found with Date Created: ${dateCreated}`
          );
      },
    });
  }

  owner_findByPetId() {
    Swal.fire({
      title: 'Find by Pet ID',
      html: "<input type='number' class='form-control' id='petID' />",
      confirmButtonText: 'Find',
      showCloseButton: true,
      preConfirm: () => {
        let id = Number($('#petID').val());

        if (!id) Swal.showValidationMessage('Please enter Pet ID to find.');

        return { petID: id };
      },
    }).then((result) => {
      if (result.isConfirmed) {
        this.sub = this.ownerService
          .findByPetId(result.value!.petID)
          .subscribe({
            next: (owner) => {
              this.router.navigate([
                `/owner/findByPetId/${result.value!.petID}`,
              ]);
            },
            error: (err) => {
              this.dm.displayError(err);
            },
          });
      }
    });
  }

  owner_findByPetName() {
    Swal.fire({
      title: 'Find by Pet Name',
      html: "<input type='text' class='form-control' id='petName' />",
      confirmButtonText: 'Find',
      showCloseButton: true,
      preConfirm: () => {
        let name = String($('#petName').val());

        if (!name) Swal.showValidationMessage('Please enter Pet Name to find.');

        return { petName: name };
      },
    }).then((result) => {
      if (result.isConfirmed) {
        this.sub = this.ownerService
          .findByPetName(result.value!.petName)
          .subscribe({
            next: (ownerList) => {
              this.ownerList = ownerList
            },
            error: (err) => {
              this.errorMessage = err,
              this.dm.displayError(err);
            },
            complete: () => {
              if (this.ownerList.length > 0)
                this.router.navigate([
                  `/owner/findByPetName/${result.value!.petName}`,
                ]);
              else
                this.dm.displayError(this.errorMessage);
            }
          });
      }
    });
  }

}
