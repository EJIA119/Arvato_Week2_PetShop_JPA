import { Location } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { IOwner } from 'src/app/owner/owner';
import { OwnerService } from 'src/app/owner/owner.service';
import { DisplayMessage } from 'src/app/shared/display-message';
import Swal from 'sweetalert2';
import { IPet } from '../pet';
import { PetFunction } from '../pet.function';
import { PetService } from '../pet.service';

@Component({
  selector: 'app-pet-detail',
  templateUrl: './pet-detail.component.html',
  styleUrls: ['./pet-detail.component.css']
})
export class PetDetailComponent implements OnInit {

  pageTitle = "Pet Detail";

  pet!: IPet;
  owner!: IOwner;

  sub!: Subscription;
  errorMessage: string = "";


  constructor(private petService : PetService,
    private route: ActivatedRoute,
    private location: Location,
    private router: Router,
    private ownerService : OwnerService,
    private petFunction : PetFunction,
    private dm:DisplayMessage) { }

  ngOnInit(): void {

    const id = Number(this.route.snapshot.paramMap.get("id"));

    this.sub = this.petService.findById(id).subscribe({
      next: (pet) => this.pet = pet,
      error: (err) => this.errorMessage = err
    });

    this.sub = this.ownerService.findByPetId(id).subscribe({
      next: (owner) => this.owner = owner,
      error: (err) => this.errorMessage = err
    })

  }

  pet_deleteById(id : number){

    Swal.fire({
      icon:'info',
      text:"Remove this pet?",
      confirmButtonText: "Yes, remove",
      showCancelButton: true,
      cancelButtonText: "No, cancel"
    }).then((result) => {
      if(result.isConfirmed){
        this.petService.delete(id).subscribe({
          error: (err) => {
            this.errorMessage = err,
            this.dm.displayError(this.errorMessage)
          },
          complete: () => {
            Swal.fire({
              icon:'success',
              text:"Pet record has been remove."
            }).then( () => {
              this.location.back();
            })
          }
        });
      }
    });

    
  }

  navigateBack(){
    this.location.back();
    // this.router.navigate(["/home"]);
  }

}
