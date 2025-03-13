package dev.shorturl.services;

import dev.shorturl.dto.link.CompleteLinkDTO;
import dev.shorturl.dto.link.CompleteLinkRequestDTO;
import dev.shorturl.exception.LinkNotFoundException;
import dev.shorturl.model.AppUser;
import dev.shorturl.model.Link;
import dev.shorturl.model.Tag;
import dev.shorturl.repository.LinkRepository;
import dev.shorturl.repository.TagRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class LinkService {
  private final LinkRepository linkRepository;
  private final TagRepository tagRepository;
  private final SlugService slugService;

  public LinkService(LinkRepository linkRepository, TagRepository tagRepository, SlugService slugService) {
    this.linkRepository = linkRepository;
    this.tagRepository = tagRepository;
    this.slugService = slugService;
  }

  @Transactional
  public CompleteLinkDTO createLink(CompleteLinkRequestDTO completeLinRequestkDTO, AppUser user) {
    List<Tag> tags = tagRepository.findAllById(completeLinRequestkDTO.tagsIds());
    Link link = Link.builder()
        .url(completeLinRequestkDTO.url())
        .slug(slugService.generateSlug())
        .createdAt(LocalDateTime.now())
        .expiresAt(LocalDateTime.now().plusDays(30))
        .description(completeLinRequestkDTO.description())
        .tags(Set.copyOf(tags))
        .appUser(user)
        .build();
    linkRepository.save(link);
    return CompleteLinkDTO.of(link);
  }

  public CompleteLinkDTO getLinkById(Long id) {
    Link link = linkRepository.findById(id).orElseThrow(() -> new LinkNotFoundException(id));
    return CompleteLinkDTO.of(link);
  }

  public List<CompleteLinkDTO> getAllLinks(AppUser user) {
    return linkRepository.findByAppUser(user).stream()
        .map(CompleteLinkDTO::of)
        .collect(Collectors.toList());

  }

  @Transactional
  public CompleteLinkDTO updateLink(Long id, CompleteLinkRequestDTO completeLinkRequestDTO, AppUser user1) {
    Link link = linkRepository.findById(id).orElseThrow(() -> new LinkNotFoundException(id));
    if (!link.getAppUser().equals(user1)) {
      throw new SecurityException("User not authorized to update this link");
    }
    List<Tag> tags = tagRepository.findAllById(completeLinkRequestDTO.tagsIds());
    link.setUrl(completeLinkRequestDTO.url());
    link.setSlug(slugService.generateSlug());
    link.setDescription(completeLinkRequestDTO.description());
    link.setTags(Set.copyOf(tags));
    link.setExpiresAt(LocalDateTime.now().plusDays(30));
    linkRepository.save(link);
    return CompleteLinkDTO.of(link);
  }

  @Transactional
  public void deleteLink(Long id, AppUser user) {
    Link link = linkRepository.findById(id).orElseThrow(() -> new LinkNotFoundException(id));
    if (!link.getAppUser().equals(user)) {
      throw new SecurityException("User not authorized to delete this link");
    }
    linkRepository.delete(link);
  }
}
