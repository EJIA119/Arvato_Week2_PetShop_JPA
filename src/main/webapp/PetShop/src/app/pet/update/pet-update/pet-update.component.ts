import { DatePipe, Location } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { IOwner } from 'src/app/owner/owner';
import { OwnerService } from 'src/app/owner/owner.service';
import Swal from 'sweetalert2';
import { IPet } from '../../pet';
import { PetService } from '../../pet.service';

@Component({
  selector: 'app-pet-update',
  templateUrl: './pet-update.component.html',
  styleUrls: ['./pet-update.component.css'],
  providers: [DatePipe]
})
export class PetUpdateComponent implements OnInit {
  pageTitle = 'Pet Detail';

  owner: IOwner | undefined;
  ownerList: IOwner[] = [];

  pet!: IPet;

  errorMessage: string = '';
  sub!: Subscription;

  selectedOwnerID: number | undefined;

  constructor(
    private petService: PetService,
    private ownerService: OwnerService,
    private route: ActivatedRoute,
    private router: Router,
    private datePipe: DatePipe,
    private location: Location
  ) {}

  updatePetForm = new FormGroup({
    name: new FormControl('', Validators.required),
    breed: new FormControl('', Validators.required),
    ownerID: new FormControl(0),
  });

  ngOnInit(): void {
    // this.pet = {
    //   id : 0,
    //   name: "",
    //   breed:"",
    //   date_created: this.datePipe.transform(new Date(), 'yyyy-MM-dd') || '',
    //   date_modified: this.datePipe.transform(new Date(), 'yyyy-MM-dd') || '',
    // }

    


    const id = Number(this.route.snapshot.paramMap.get('id'));

    this.pageTitle += `: ${id}`;

    this.sub = this.petService.findById(id).subscribe({
      next: (pet) => (this.pet = pet),
      error: (err) => {
        Swal.fire({
          icon: 'error',
          title: 'No Pet record found.',
        }).then((result) => {
          this.router.navigate(['/pet']);
        });
        this.errorMessage = err;
      },
      complete: () => {
        this.updatePetForm.get('name')?.setValue(this.pet.name);
        this.updatePetForm.get('breed')?.setValue(this.pet.breed);
      }
    });

    this.sub = this.ownerService.findByPetId(id).subscribe({
      next: (owner) => (this.owner = owner),
      error: (err) => {
        
        this.errorMessage = err;
      },
      complete: () => {
        if(this.owner != null){
          this.selectedOwnerID = this.owner.id;
          this.updatePetForm.get('ownerID')?.setValue(this.selectedOwnerID);
        }
      }
    });

    this.sub = this.ownerService.findAll().subscribe({
      next: (ownerList) => this.ownerList = ownerList,
      error: (err) => this.errorMessage = err
    })
  }

  

  onSubmit() {
    this.pet.name = this.updatePetForm.get('name')?.value?.trim() || "";
    this.pet.breed = this.updatePetForm.get('breed')?.value?.trim() || "";
    this.pet.date_modified = this.datePipe.transform(new Date(), 'yyyy-MM-dd')!;
    this.selectedOwnerID = this.updatePetForm.get('ownerID')?.value || 0;

    console.log("HERE, updating relation : " + this.selectedOwnerID);

    this.petService.update(this.pet).subscribe({
      next: (updatedPet) => this.pet = updatedPet,
      error: (err) => this.errorMessage = err,
      complete: () => {

        

        if(this.selectedOwnerID){
          console.log("HERE, updating relation");
          this.ownerService.createOrUpdateRelation(this.selectedOwnerID, this.pet).subscribe({
            error: (err) => this.errorMessage += "\n" + err
          });

          this.updatePetForm.setValue({
            name: this.pet.name,
            breed: this.pet.breed,
            ownerID: this.ownerList[this.selectedOwnerID -1].id
          })
        }

        

        Swal.fire({
          icon:'success',
          text:"Pet detail updated!",
        }).then((result) => {
          if(result.isConfirmed)
            this.location.back();
        });
      }
    });
  }

  navigateBack(){
    this.location.back();
  }
}
