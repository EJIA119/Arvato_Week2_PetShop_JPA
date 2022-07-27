import { Location } from '@angular/common';
import { ThisReceiver } from '@angular/compiler';
import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { Subscription } from 'rxjs';
import { IPet } from 'src/app/pet/pet';
import { PetService } from 'src/app/pet/pet.service';
import { DisplayMessage } from 'src/app/shared/display-message';
import Swal from 'sweetalert2';
import { IOwner } from '../owner';
import { OwnerFunction } from '../owner-funtions';
import { OwnerService } from '../owner.service';

@Component({
  selector: 'app-owner-detail',
  templateUrl: './owner-detail.component.html',
  styleUrls: ['./owner-detail.component.css'],
})
export class OwnerDetailComponent implements OnInit {
  pageTitle = 'Owner Detail';
  owner: IOwner | undefined;

  petList: IPet[] = [];

  selectedPetID: string = "";
  selectedPet!: IPet;
  availablePetList: IPet[] = [];

  errorMessage: string = '';
  sub!: Subscription;

  id: number | undefined;

  currentPath: string = "";

  ownershipForm = new FormGroup({
    availablePet : new FormControl('',Validators.required)
  });

  constructor(
    private ownerService: OwnerService,
    private route: ActivatedRoute,
    private router: Router,
    private location: Location,
    private petService: PetService,
    private modalService: NgbModal,
    private dm: DisplayMessage
  ) {}

  ngOnInit(): void {
    if (this.route.snapshot.url[1].path.match('findById')) {
      this.id = Number(this.route.snapshot.paramMap.get('id'));

      this.pageTitle += `: Find by Owner ID : ${this.id}`;

      this.sub = this.ownerService.findById(this.id).subscribe({
        next: (owner) => (this.owner = owner),
        error: (err) => (this.errorMessage = err),
        complete: () => {
          this.displayPetList();
        }
      });
    } else if (this.route.snapshot.url[1].path.match('findByPetId')) {
      const petID = Number(this.route.snapshot.paramMap.get('id'));

      this.pageTitle += `: Find By Pet ID : ${petID}`;

      this.sub = this.ownerService.findByPetId(petID).subscribe({
        next: (owner) => (this.owner = owner),
        error: (err) => (this.errorMessage = err),
        complete: () => {
          this.displayPetList();
        }
      });
    }

    this.sub = this.petService.orphanPet().subscribe({
      next: (orphanPetList) => this.availablePetList = orphanPetList
    })
  }

  owner_addPet(content: any){

    this.sub = this.petService.orphanPet().subscribe({
      next: (petList) => this.availablePetList = petList,
      error: (err) => this.errorMessage = err,
    });

    this.modalService.open(content, {ariaLabelledBy: 'modal-add-pet'});

  }

  onSubmit(){
    this.selectedPetID = this.ownershipForm.get('availablePet')?.value || '';
    this.sub = this.petService.findById(Number(this.selectedPetID)).subscribe({
      next: (pet) => this.selectedPet = pet,
      error: (err) => {
        this.errorMessage = err,
        this.dm.displayError(this.errorMessage)
      },
      complete: () => {
        this.sub = this.ownerService.createOrUpdateRelation(Number(this.id), this.selectedPet).subscribe({
          error: (err) => {
            this.errorMessage = err,
            this.dm.displayError(this.errorMessage)
          },
          complete: () => {
            this.dm.displaySuccess("The selected pet is now owned by this owner.");
            this.displayPetList();
          }
        })
      }

    });
  }



  navigateBack() {
    this.location.back();
  }

  displayPetList() {
    this.sub = this.ownerService.findPetByOwnerId(this.owner?.id!).subscribe({
      next: (petList) => (this.petList = petList),
      error: (err) => (this.errorMessage = err),
    });
  }
}
