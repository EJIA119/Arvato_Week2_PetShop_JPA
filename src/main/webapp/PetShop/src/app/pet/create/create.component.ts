import { DatePipe, Location } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Subscription } from 'rxjs';
import { IOwner } from 'src/app/owner/owner';
import { OwnerService } from 'src/app/owner/owner.service';
import { DisplayMessage } from 'src/app/shared/display-message';
import Swal from 'sweetalert2';
import { IPet } from '../pet';
import { PetFunction } from '../pet.function';
import { PetService } from '../pet.service';

@Component({
  selector: 'app-create',
  templateUrl: './create.component.html',
  styleUrls: ['./create.component.css'],
  providers:[DatePipe]
})
export class CreateComponent implements OnInit {
  pageTitle = 'Create Pet';
  pet!: IPet;
  ownerList: IOwner[] = [];

  selectedOwnerID: number | undefined;

  sub!: Subscription;
  errorMessage: string = '';

  createPetForm = new FormGroup({
    name: new FormControl('', Validators.required),
    breed: new FormControl('', Validators.required),
    owner: new FormControl(0)
  });

  constructor(
    private petService: PetService,
    private location: Location,
    private ownerService: OwnerService,
    private petFunction: PetFunction,
    private datePipe: DatePipe,
    private dm: DisplayMessage
  ) {
    this.pet = {
      id: 0,
      name:"",
      breed: "",
      date_created: this.datePipe.transform(new Date(), 'yyyy-MM-dd') || '',
      date_modified: this.datePipe.transform(new Date(), 'yyyy-MM-dd') || ''
    }
  }

  ngOnInit(): void {
    this.sub = this.ownerService.findAll().subscribe({
      next: (ownerList) => this.ownerList = ownerList,
      error: (err) => this.errorMessage = err
    });
  }

  onSubmit() {
    this.pet.name = this.createPetForm.get('name')?.value || '',
    this.pet.breed= this.createPetForm.get('breed')?.value || '',
    this.selectedOwnerID = this.createPetForm.get('owner')?.value || 0,



    this.petService.create(this.pet).subscribe({
      next: (newPet) => this.pet = newPet,
      error: (err) => {
        this.errorMessage = err,
        this.dm.displayError(this.errorMessage)
      },
      complete: () => {

        if(this.selectedOwnerID){
          this.ownerService.createOrUpdateRelation(this.selectedOwnerID, this.pet).subscribe({
            error: (err) => this.errorMessage += "\n" + err
          });
        }
          

        if(this.errorMessage == ''){
          Swal.fire({
            icon:'success',
            text:"New Pet added successfully"
          }).then((result) => {
            if(result.isConfirmed){
              this.createPetForm.reset();
              this.location.back();
            }
          });
        }
      }
    })


  }

  navigateBack() {
    this.location.back();
  }
}
